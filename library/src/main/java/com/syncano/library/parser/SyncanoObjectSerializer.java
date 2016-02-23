package com.syncano.library.parser;

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
    final boolean serializeReadOnlyFields;

    public SyncanoObjectSerializer(boolean serializeReadOnlyFields) {
        this.serializeReadOnlyFields = serializeReadOnlyFields;
    }

    public JsonElement serialize(SyncanoObject localObject, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(localObject.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String keyName = SyncanoClassHelper.getFieldName(field);
                if (localObject.isOnClearList(keyName)) {
                    jsonObject.add(keyName, jsc.serialize(null));
                    continue;
                }
                if (shouldSkipField(field, localObject))
                    continue;
                JsonElement jsonElement = toJsonObject(localObject, field, jsc);
                jsonObject.add(keyName, jsonElement);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private boolean shouldSkipField(Field f, SyncanoObject localObject) throws IllegalAccessException {
        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);
        // Don't serialize read only fields (like "id" or "created_at").
        // We want only to receive it, not send.
        // SyncanoFile is handled in SendRequest
        if (syncanoField == null ||
                (!serializeReadOnlyFields && syncanoField.readOnly() && !syncanoField.required()) ||
                f.getDeclaringClass().isAssignableFrom(SyncanoFile.class) ||
                f.get(localObject) == null) {
            return true;
        }

        return false;
    }


    private JsonElement toJsonObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
        if (SyncanoClassHelper.findType(f) == FieldType.REFERENCE) {
            return serializeReference(localObject, f);
        } else {
            return serializePlainObject(localObject, f, jsc);
        }
    }

    private JsonElement serializeReference(SyncanoObject localObject, Field f) throws IllegalAccessException {
        SyncanoObject syncanoObject = (SyncanoObject) f.get(localObject);
        if (syncanoObject == null)
            return new JsonPrimitive("");
        Integer id = syncanoObject.getId();
        if (id == null) {
            return new JsonPrimitive("");
        }
        return new JsonPrimitive(id);
    }

    private JsonElement serializePlainObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
        return jsc.serialize(f.get(localObject), f.getType());
    }
}