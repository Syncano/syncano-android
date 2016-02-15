package com.syncano.library.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.syncano.library.Constants;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class SyncanoObjectDeserializer implements JsonDeserializer<SyncanoObject> {
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
        GsonParser.GsonParseConfig gsonParseConfig = new GsonParser.GsonParseConfig();
        gsonParseConfig.deserializeNull = false;
        GsonParser.createGson(f.get(syncanoObject), gsonParseConfig).fromJson(jsonObject, f.getType());
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
