package com.syncano.library.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

public class SyncanoObjectDeserializer implements JsonDeserializer<SyncanoObject> {
    private final static String FIELD_VALUE = "value";
    private SyncanoObject syncanoObject = null;
    private GsonParser.GsonParseConfig config;

    public SyncanoObjectDeserializer(Object syncanoObject, GsonParser.GsonParseConfig config) {
        if (syncanoObject != null && syncanoObject instanceof SyncanoObject) {
            this.syncanoObject = (SyncanoObject) syncanoObject;
        }
        this.config = config;
    }

    public SyncanoObject deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return parseSyncanoObject(syncanoObject, type, je, jdc);
    }

    private void setFieldFromJsonElement(SyncanoObject o, Field f, JsonDeserializationContext jdc, JsonElement je) {
        try {
            // clear value when received null, or value not received
            if (je == null || je.isJsonNull()) {
                Class<?> type = f.getType();
                if (type.isPrimitive()) {
                    // set default value for primitive types
                    if (type.equals(int.class)) {
                        f.set(o, 0);
                    } else if (type.equals(boolean.class)) {
                        f.set(o, false);
                    } else if (type.equals(byte.class)) {
                        f.set(o, (byte) 0);
                    } else if (type.equals(short.class)) {
                        f.set(o, (short) 0);
                    } else if (type.equals(long.class)) {
                        f.set(o, 0L);
                    } else if (type.equals(float.class)) {
                        f.set(o, 0f);
                    } else if (type.equals(double.class)) {
                        f.set(o, 0d);
                    } else if (type.equals(char.class)) {
                        f.set(o, '\u0000');
                    }
                } else {
                    // set null for objects
                    f.set(o, null);
                }
                // when received not null value for this field
            } else {
                // when it's a reference parse as SyncanoObject
                if (SyncanoClassHelper.findType(f) == FieldType.REFERENCE) {
                    f.set(o, parseSyncanoObject((SyncanoObject) f.get(o), f.getType(), je, jdc));
                    // when it's not reference parse using default gson methods
                } else {
                    f.set(o, jdc.deserialize(je, f.getType()));
                }
            }
        } catch (IllegalAccessException e) {
            // shouldn't happen because field is set to be accessible
            e.printStackTrace();
        }
    }

    private SyncanoObject parseSyncanoObject(SyncanoObject syncanoObject, Type type, JsonElement je, JsonDeserializationContext jdc) {
        // when received only reference id as a json primitive
        if (je.isJsonPrimitive() && !je.isJsonNull()) {
            return createSyncanoObjectWithId(syncanoObject, type, je.getAsInt());
        }

        // when received only reference id in a json object
        JsonObject jo = je.getAsJsonObject();
        if (!jo.has(Entity.FIELD_ID) && jo.has(FIELD_VALUE)) {
            return createSyncanoObjectWithId(syncanoObject, type, jo.get(FIELD_VALUE).getAsInt());
        }

        // when received full syncano object parse it
        if (syncanoObject == null) {
            syncanoObject = createSyncanoObject((Class<? extends SyncanoObject>) type);
            // important to call it because sometimes fields that are always instantiated, here are nulls
            syncanoObject.resetRequestBuildingFields();
        }
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields((Class) type);
        for (Field field : fields) {
            SyncanoField annotation = field.getAnnotation(SyncanoField.class);
            if (annotation.onlyLocal() && !config.forLocalStorage) {
                continue;
            }
            field.setAccessible(true);
            String syncanoKey;
            if (config.forLocalStorage) {
                syncanoKey = SyncanoClassHelper.getOfflineFieldName(field);
            } else {
                syncanoKey = SyncanoClassHelper.getFieldName(field);
            }
            JsonElement syncanoElement = jo.get(syncanoKey);
            setFieldFromJsonElement(syncanoObject, field, jdc, syncanoElement);
        }
        return syncanoObject;
    }

    private SyncanoObject createSyncanoObjectWithId(SyncanoObject oldObject, Type type, int id) {
        if (oldObject == null || oldObject.getId() == null || id != oldObject.getId()) {
            SyncanoObject newObject = createSyncanoObject((Class<? extends SyncanoObject>) type);
            newObject.setId(id);
            return newObject;
        }
        return oldObject;
    }

    public static SyncanoObject createSyncanoObject(Class<? extends SyncanoObject> clazz) {
        try {
            Constructor<? extends SyncanoObject> defaultConstructor = clazz.getConstructor();
            defaultConstructor.setAccessible(true);
            return defaultConstructor.newInstance();
        } catch (Exception e) {
            //Workaround if no empty constructor provided
            Gson gson = new GsonBuilder()
                    .setFieldNamingStrategy(new SyncanoFieldNamingStrategy()).create();
            return gson.fromJson("{}", clazz);
        }
    }
}