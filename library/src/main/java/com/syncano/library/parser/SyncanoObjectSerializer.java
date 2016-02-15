package com.syncano.library.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

class SyncanoObjectSerializer implements JsonSerializer<SyncanoObject> {
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
        if (syncanoObject == null)
            return new JsonPrimitive("");
        Integer id = syncanoObject.getId();
        if (id == null) {
            SyncanoLog.e("Syncano-Serializer", localObject.getClass().getName() + " syncano object cannot be automatic created. Inner object is skipped.");
            throw new IllegalAccessException();
        }
        return new JsonPrimitive(id);
    }

    private JsonElement serializablePlainObject(SyncanoObject localObject, Field f, JsonSerializationContext jsc) throws IllegalAccessException {
        return jsc.serialize(f.get(localObject), f.getType());
    }
}
