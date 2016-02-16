package com.syncano.library.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;

public class SyncanoObjectAdapterFactory extends CustomizedTypeAdapterFactory<SyncanoObject> {

    private final static String FIELD_VALUE = "value";

    public SyncanoObjectAdapterFactory(Class<SyncanoObject> customizedClass) {
        super(customizedClass);
    }

    /*
    Properly adding references data to json
     */
    @Override
    protected void beforeWrite(final SyncanoObject source, final JsonElement toSerialize) {
        // Find a field that is a reference and send it as id, not as a full object
        findReference(source.getClass(), new ReferenceFinder() {
            @Override
            public void found(Field field) {
                field.setAccessible(true);
                SyncanoObject fieldObj = null;
                try {
                    fieldObj = (SyncanoObject) field.get(source);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (fieldObj != null) {
                    toSerialize.getAsJsonObject().addProperty(SyncanoClassHelper.getFieldName(field), fieldObj.getId());
                }
            }
        });
    }

    /*
    Converting ugly reference description to easier to parse object with id
     */
    @Override
    protected void afterRead(TypeToken<SyncanoObject> type, final JsonElement deserialized) {
        if (type.getType() instanceof Class) {
            findReference((Class) type.getType(), new ReferenceFinder() {
                @Override
                public void found(Field field) {
                    String fieldName = SyncanoClassHelper.getFieldName(field);
                    JsonObject deserializedObject = deserialized.getAsJsonObject();
                    JsonObject refData = deserializedObject.get(fieldName).getAsJsonObject();
                    if (!refData.has(Entity.FIELD_ID) && refData.has(FIELD_VALUE)) {
                        JsonObject newRefObject = new JsonObject();
                        newRefObject.addProperty(Entity.FIELD_ID, refData.get(FIELD_VALUE).getAsString());
                        deserializedObject.add(fieldName, newRefObject);
                    }
                }
            });
        }
    }

    private void findReference(Class clazz, ReferenceFinder f) {
        for (Field field : clazz.getDeclaredFields()) {
            SyncanoField fieldAnnotation = field.getAnnotation(SyncanoField.class);
            if (fieldAnnotation == null) {
                continue;
            }
            FieldType type = SyncanoClassHelper.findType(field, fieldAnnotation);
            if (type == null || !FieldType.REFERENCE.equals(type)) {
                continue;
            }
            f.found(field);
        }
    }

    interface ReferenceFinder {
        void found(Field field);
    }
}
