package com.syncano.library.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoFile;

public class GsonHelper {

    public static Gson createGson() {
        return createGson(null);
    }

    public static <T> Gson createGson(final T object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoFile.class, new FileDeserializer());
        gsonBuilder.addSerializationExclusionStrategy(new SyncanoSerializationStrategy());
        gsonBuilder.addDeserializationExclusionStrategy(new SyncanoDeserializationStrategy());
        gsonBuilder.setFieldNamingStrategy(new SyncanoFieldNamingStrategy());

        // it makes possible to fill existing object instead of creating new one
        if (object != null) {
            gsonBuilder.registerTypeAdapter(object.getClass(), new InstanceCreator<T>() {
                @Override
                public T createInstance(Type type) {
                    return object;
                }
            });
        }
        return gsonBuilder.create();
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

    private static class FileDeserializer implements JsonDeserializer<SyncanoFile> {
        public SyncanoFile deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            String link = json.getAsJsonObject().get("value").getAsString();
            return new SyncanoFile(link);
        }
    }

    private static class SyncanoSerializationStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {

            SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);

            // Don't serialize read only fields (like "id" or "created_at").
            // We want only to receive it, not send.
            // don't serialize more complicated data structures as SyncanoFile
            if (syncanoField == null || (syncanoField.readOnly() && !syncanoField.required()) ||
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
}
