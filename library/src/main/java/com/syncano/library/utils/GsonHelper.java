package com.syncano.library.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.Constants;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class GsonHelper {

    public static Gson createGson() {
        return createGson(new GsonConfig());
    }

    public static <T> Gson createGson(GsonConfig config) {
        return createGson(null, config);
    }

    public static <T> Gson createGson(T object) {
        return createGson(object, new GsonConfig());
    }

    public static <T> Gson createGson(final T object, GsonConfig config) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoHashSet.class, new SyncanoHashSetDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoHashSet.class, new SyncanoHashSetSerializer());
        gsonBuilder.registerTypeAdapter(SyncanoFile.class, new FileDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(SyncanoObject.class, new SyncanoObjectSerializer());
        gsonBuilder.addSerializationExclusionStrategy(new SyncanoSerializationStrategy(config.readOnlyNotImportant));
        gsonBuilder.addDeserializationExclusionStrategy(new SyncanoDeserializationStrategy());
        gsonBuilder.setFieldNamingStrategy(new SyncanoFieldNamingStrategy());

        // it makes possible to fill existing object instead of creating new one
        if (object != null) {
            if (object instanceof SyncanoObject) {
                gsonBuilder.registerTypeHierarchyAdapter(SyncanoObject.class, new SyncanoObjectDeserializer((SyncanoObject) object, config.deserializeNull));
            }
            gsonBuilder.registerTypeAdapter(object.getClass(), new InstanceCreator<T>() {
                @Override
                public T createInstance(Type type) {
                    return object;
                }
            });
        }
        return gsonBuilder.create();
    }

    public static class GsonConfig {
        public boolean readOnlyNotImportant = false;
        public boolean deserializeNull = false;
    }

    private static class DateSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsc) {
            return new JsonPrimitive(DateTool.parseDate(date));
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            String dateString;
            if (json.isJsonPrimitive()) {
                dateString = json.getAsJsonPrimitive().getAsString();
            } else {
                dateString = json.getAsJsonObject().get("value").getAsString();
            }
            return DateTool.parseString(dateString);
        }
    }

    private static class SyncanoHashSetDeserializer implements JsonDeserializer<SyncanoHashSet> {
        public SyncanoHashSet deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            SyncanoHashSet syncanoHashSet = new SyncanoHashSet();
            String[] spitedString = json.getAsJsonPrimitive().getAsString().split(",");
            Collections.addAll(syncanoHashSet, spitedString);
            return syncanoHashSet;
        }
    }

    private static class SyncanoHashSetSerializer implements JsonSerializer<SyncanoHashSet> {
        public JsonElement serialize(SyncanoHashSet syncanoHashSet, Type type, JsonSerializationContext jsc) {
            return new JsonPrimitive(syncanoHashSet.getAsString());
        }
    }

    private static class FileDeserializer implements JsonDeserializer<SyncanoFile> {
        public SyncanoFile deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            String link = json.getAsJsonObject().get("value").getAsString();
            return new SyncanoFile(link);
        }
    }

    private static class SyncanoSerializationStrategy implements ExclusionStrategy {

        private boolean readOnlyNotImportant = false;

        public SyncanoSerializationStrategy(boolean readOnlyNotImportant) {
            this.readOnlyNotImportant = readOnlyNotImportant;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {

            SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);

            // Don't serialize read only fields (like "id" or "created_at").
            // We want only to receive it, not send.
            // don't serialize more complicated data structures as SyncanoFile
            if (syncanoField == null ||
                    (!readOnlyNotImportant && syncanoField.readOnly() && !syncanoField.required()) ||
                    f.getDeclaredClass().isAssignableFrom(SyncanoFile.class)) {
                return true;
            }

            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static class SyncanoDeserializationStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);
            if (syncanoField == null) {
                return true;
            }
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static class SyncanoFieldNamingStrategy implements FieldNamingStrategy {

        @Override
        public String translateName(Field f) {
            return SyncanoClassHelper.getFieldName(f);
        }
    }

    public static class SyncanoObjectDeserializer implements JsonDeserializer<SyncanoObject> {
        private final SyncanoObject syncanoObject;
        private final boolean deserializeNull;

        public SyncanoObjectDeserializer(SyncanoObject syncanoObject, boolean deserializeNull) {
            this.syncanoObject = syncanoObject;
            this.deserializeNull = deserializeNull;
        }


        public SyncanoObject deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            JsonObject jsonObject = je.getAsJsonObject();

            List<Field> fields = SyncanoClassHelper.getInheritedFields(syncanoObject.getClass());
            for (Field field : fields) {
                SyncanoField syncanoField = field.getAnnotation(SyncanoField.class);
                if (syncanoField != null) {
                    field.setAccessible(true);
                    String syncanoKey = syncanoField.name();
                    JsonElement syncanoElement = jsonObject.get(syncanoKey);
                    setFieldFromJsonElement(field, jdc, syncanoElement);
                }
            }
            return syncanoObject;

        }

        private void setFieldFromJsonElement(Field f, JsonDeserializationContext jdc, JsonElement syncanoElement) {
            try {
                if (SyncanoObject.class.isAssignableFrom(f.getType())) {
                    JsonObject jsonObject = syncanoElement.getAsJsonObject();
                    if (isSyncanoReference(jsonObject)) {
                        parseReference(f, jsonObject);
                    } else {
                        parseSyncanoObject(f, jsonObject);
                    }
                } else {
                    parsePlainObject(jdc, f, syncanoElement);
                }
            } catch (IllegalAccessException ignore) {
            }
        }


        private void parsePlainObject(JsonDeserializationContext jdc, Field f, JsonElement syncanoElement) throws IllegalAccessException {
            Object obj = jdc.deserialize(syncanoElement, f.getType());
            if (obj != null || deserializeNull)
                f.set(syncanoObject, obj);
        }

        private void parseSyncanoObject(Field f, JsonObject jsonObject) throws IllegalAccessException {
            GsonConfig gsonConfig = new GsonConfig();
            gsonConfig.deserializeNull = false;
            createGson(f.get(syncanoObject), gsonConfig).fromJson(jsonObject, f.getType());
        }

        private void parseReference(Field f, JsonObject jsonObject) throws IllegalAccessException {
            SyncanoObject syncanoObjectField = (SyncanoObject) f.get(syncanoObject);
            syncanoObjectField.setId(jsonObject.get("value").getAsInt());
        }

        private boolean isSyncanoReference(JsonObject jsonObject) {
            if (jsonObject == null)
                return false;
            JsonElement jsonElement = jsonObject.get(Constants.FIELD_TYPE);
            return jsonElement != null && FieldType.REFERENCE.name().equals(jsonElement.getAsString());

        }
    }

    private static class SyncanoObjectSerializer implements JsonSerializer<SyncanoObject> {
        public JsonElement serialize(SyncanoObject localObject, Type type, JsonSerializationContext jsc) {
            JsonObject jsonObject = new JsonObject();
            List<Field> fields = SyncanoClassHelper.getInheritedFields(localObject.getClass());
            for (Field field : fields) {
                SyncanoField syncanoField = field.getAnnotation(SyncanoField.class);
                if (syncanoField != null) {
                    field.setAccessible(true);
                    try {
                        String keyName = syncanoField.name();
                        JsonElement jsonElement = serializableFieldToJsonObject(localObject, field, jsc);
                        jsonObject.add(keyName, jsonElement);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonObject;
        }


        private JsonElement serializableFieldToJsonObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
            if (SyncanoObject.class.isAssignableFrom(f.getType())) {
                return serializableSyncanoObject(localObject, f, jsc);
            } else {
                return serializablePlainObject(localObject, f, jsc);
            }
        }

        private JsonElement serializableSyncanoObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
            SyncanoObject syncanoObject = (SyncanoObject) f.get(localObject);
            Integer id = syncanoObject.getId();
            if (id == null) {
                SyncanoLog.e("Syncano-Serializer", localObject.getClass().getName() + " syncano object cannot be automatic created. Inner object is skipped");
                throw new IllegalAccessException();
            }
            return new JsonPrimitive(id);
        }

        private JsonElement serializablePlainObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
            return jsc.serialize(f.get(localObject), f.getType());
        }
    }

}
