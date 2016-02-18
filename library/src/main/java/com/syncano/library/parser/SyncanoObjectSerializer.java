package com.syncano.library.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;

class SyncanoObjectSerializer implements JsonSerializer<SyncanoObject> {
    public JsonElement serialize(SyncanoObject localObject, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(localObject.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                String keyName = SyncanoClassHelper.getFieldName(field);
                JsonElement jsonElement = toJsonObject(localObject, field, jsc);
                jsonObject.add(keyName, jsonElement);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
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