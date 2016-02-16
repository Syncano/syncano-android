package com.syncano.library.parser;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.syncano.library.Constants;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

/*
Custom deserializer for SyncanoObject is only use if you pass non null object to parser
If object is null parser use default deserializer
 */
public class SyncanoObjectDeserializer implements JsonDeserializer<SyncanoObject> {
    private final SyncanoObject syncanoObject;

    public SyncanoObjectDeserializer(SyncanoObject syncanoObject) {
        this.syncanoObject = syncanoObject;
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
                parseSyncanoObject(f, syncanoElement);
            } else {
                parsePlainObject(jdc, f, syncanoElement);
            }
        } catch (IllegalAccessException ignore) {
        }
    }

    private void parseSyncanoObject(Field f, JsonElement syncanoElement) throws IllegalAccessException {
        if (syncanoElement == null || syncanoElement.isJsonNull()) {
            parseNullReference(f);
        } else {
            JsonObject jsonObject = syncanoElement.getAsJsonObject();
            if (isSyncanoReference(jsonObject)) {
                parseReference(f, jsonObject);
            } else {
                parseFullSyncanoObject(f, jsonObject);
            }
        }
    }

    private void parseNullReference(Field f) throws IllegalAccessException {
        f.set(syncanoObject, null);
    }


    private void parsePlainObject(JsonDeserializationContext jdc, Field f, JsonElement syncanoElement) throws IllegalAccessException {
        Object obj = jdc.deserialize(syncanoElement, f.getType());
        if (obj != null)
            f.set(syncanoObject, obj);
    }

    /*
    At this moment it is dead code but in future syncano could query object with expanded field
     */
    private void parseFullSyncanoObject(Field f, JsonObject jsonObject) throws IllegalAccessException {
        GsonParser.createGson(f.get(syncanoObject)).fromJson(jsonObject, f.getType());
    }

    private void parseReference(Field f, JsonObject jsonObject) throws IllegalAccessException {
        SyncanoObject syncanoInnerObject = (SyncanoObject) f.get(syncanoObject);
        Integer syncanoId = jsonObject.get("value").getAsInt();
        //Check this field point to the same object, if not create new empty object
        if (syncanoInnerObject.getId() == null || !syncanoInnerObject.getId().equals(syncanoId)) {
            syncanoInnerObject = recreateSyncanoObject(syncanoInnerObject);
            syncanoInnerObject.setId(syncanoId);
            f.set(syncanoObject, syncanoInnerObject);
        }
    }

    private SyncanoObject recreateSyncanoObject(SyncanoObject syncanoObjectField) {
        try {
            Constructor<? extends SyncanoObject> defaultConstructor = syncanoObjectField.getClass().getConstructor();
            return defaultConstructor.newInstance();
        } catch (Exception e) {
            //Workaround if no empty constructor provided
            return new Gson().fromJson("{}", syncanoObjectField.getClass());
        }
    }

    private boolean isSyncanoReference(JsonObject jsonObject) {
        JsonElement jsonElement = jsonObject.get(Constants.FIELD_TYPE);
        return jsonElement != null && "reference".equals(jsonElement.getAsString());

    }
}
