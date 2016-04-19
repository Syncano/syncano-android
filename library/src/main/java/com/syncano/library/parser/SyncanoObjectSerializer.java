package com.syncano.library.parser;

import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

class SyncanoObjectSerializer implements JsonSerializer<SyncanoObject> {
    private GsonParser.GsonParseConfig config;

    public SyncanoObjectSerializer(GsonParser.GsonParseConfig config) {
        this.config = config;
    }

    public JsonElement serialize(SyncanoObject localObject, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(localObject.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String keyName;
                if (config.useOfflineFieldNames) {
                    keyName = SyncanoClassHelper.getOfflineFieldName(field);
                } else {
                    keyName = SyncanoClassHelper.getFieldName(field);
                }
                if (localObject.isOnClearList(keyName)) {
                    jsonObject.add(keyName, jsc.serialize(null));
                    continue;
                }
                if (shouldSkipField(field, localObject))
                    continue;
                JsonElement jsonElement = toJsonObject(localObject, field, jsc);
                if (jsonElement == null)
                    continue;
                jsonObject.add(keyName, jsonElement);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private boolean shouldSkipField(Field f, SyncanoObject localObject) throws IllegalAccessException {
        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);
        // SyncanoFile is handled in SendRequest
        if (syncanoField == null ||
                (!config.serializeReadOnlyFields && syncanoField.readOnly() && !syncanoField.required()) ||
                (f.getType().isAssignableFrom(SyncanoFile.class) && !config.serializeUrlFileFields) ||
                f.get(localObject) == null) {
            return true;
        }

        return false;
    }


    private JsonElement toJsonObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
        if (SyncanoClassHelper.findType(f) == FieldType.REFERENCE) {
            return serializeReference(localObject, f);
        } else if (f.getType().isAssignableFrom(SyncanoFile.class)) {
            SyncanoFile file = (SyncanoFile) f.get(localObject);
            if (file != null && !TextUtils.isEmpty(file.getLink())) {
                return new JsonPrimitive(file.getLink());
            }
            return null;
        } else {
            return serializePlainObject(localObject, f, jsc);
        }
    }

    private JsonElement serializeReference(SyncanoObject localObject, Field f) throws IllegalAccessException {
        SyncanoObject syncanoObject = (SyncanoObject) f.get(localObject);
        if (syncanoObject == null)
            return null;
        Integer id = syncanoObject.getId();
        if (id == null) {
            return null;
        }
        return new JsonPrimitive(id);
    }

    private JsonElement serializePlainObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
        return jsc.serialize(f.get(localObject), f.getType());
    }
}