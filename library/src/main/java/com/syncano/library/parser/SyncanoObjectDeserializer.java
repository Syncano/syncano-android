package com.syncano.library.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

    public SyncanoObjectDeserializer(Object syncanoObject) {
        if (syncanoObject != null && syncanoObject instanceof SyncanoObject) {
            this.syncanoObject = (SyncanoObject) syncanoObject;
        }
    }

    public SyncanoObject deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        return parseSyncanoObject(syncanoObject, type, je.getAsJsonObject(), jdc);
    }

    private void setFieldFromJsonElement(SyncanoObject o, Field f, JsonDeserializationContext jdc, JsonElement je) {
        try {
            if (je == null || je.isJsonNull()) {
                f.set(o, null);
            } else {
                if (SyncanoClassHelper.findType(f) == FieldType.REFERENCE) {
                    f.set(o, parseSyncanoObject((SyncanoObject) f.get(o), f.getType(), je.getAsJsonObject(), jdc));
                } else {
                    f.set(o, jdc.deserialize(je, f.getType()));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private SyncanoObject parseSyncanoObject(SyncanoObject o, Type type, JsonObject jo, JsonDeserializationContext jdc) {
        // create object if null
        if (o == null) {
            o = createSyncanoObject((Class<? extends SyncanoObject>) type);
        }
        // if received only reference id, set it and return this object
        Integer idFromReference = getIdFromReference(jo);
        if (idFromReference != null) {
            if (!idFromReference.equals(o.getId())) {
                o.setId(idFromReference);
            }
            return o;
        }

        // if received full object parse it
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields((Class) type);
        for (Field field : fields) {
            field.setAccessible(true);
            String syncanoKey = SyncanoClassHelper.getFieldName(field);
            JsonElement syncanoElement = jo.get(syncanoKey);
            setFieldFromJsonElement(o, field, jdc, syncanoElement);
        }
        return o;
    }

    private Integer getIdFromReference(JsonObject jo) {
        if (!jo.has(Entity.FIELD_ID) && jo.has(FIELD_VALUE)) {
            return jo.get(FIELD_VALUE).getAsInt();
        }
        return null;
    }

    private SyncanoObject createSyncanoObject(Class<? extends SyncanoObject> clazz) {
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