package com.syncano.library.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.Constants;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

import java.lang.reflect.Field;
import java.util.Date;

public class SyncanoClassHelper {

    /**
     * DataObjects endpoints are using class names in path.
     * Class must be marked with SyncanoClass annotation.
     *
     * @param clazz Class to extract class name.
     * @return Class name from SyncanoClass annotation.
     */
    public static String getSyncanoClassName(Class<? extends SyncanoObject> clazz) {
        SyncanoClass syncanoClass = clazz.getAnnotation(SyncanoClass.class);

        if (syncanoClass == null) {
            throw new RuntimeException("Class " + clazz.getSimpleName() + " is not marked with SyncanoClass annotation.");
        }

        return syncanoClass.name();
    }

    /**
     * Syncano objct class fields must be marked with SyncanoField annotation.
     *
     * @param clazz Class to extract fields data.
     * @return Class schema.
     */
    public static JsonArray getSyncanoClassSchema(Class<? extends SyncanoObject> clazz) {
        JsonArray schemaArray = new JsonArray();
        for (Field field : clazz.getDeclaredFields()) {
            SyncanoField fieldAnnotation = field.getAnnotation(SyncanoField.class);
            if (fieldAnnotation == null) {
                continue;
            }

            JsonObject fieldDescription = new JsonObject();
            String typeName = findType(field, fieldAnnotation);
            if (typeName == null) {
                continue;
            }

            if (typeName.equals(Constants.FIELD_TYPE_REFERENCE)) {
                String target = fieldAnnotation.target();
                if (target == null || target.isEmpty()) {
                    throw new RuntimeException("Field type " + typeName + " has to be declared together with " + Constants.FIELD_TARGET);
                } else {
                    fieldDescription.addProperty(Constants.FIELD_TARGET, target);
                }
            }
            fieldDescription.addProperty(Constants.FIELD_TYPE, typeName);
            fieldDescription.addProperty(Constants.FIELD_NAME, fieldAnnotation.name());
            if (fieldAnnotation.filterIndex()) {
                fieldDescription.addProperty(Constants.FIELD_FILTER_INDEX, true);
            }
            if (fieldAnnotation.orderIndex()) {
                fieldDescription.addProperty(Constants.FIELD_ORDER_INDEX, true);
            }
            schemaArray.add(fieldDescription);
        }

        return schemaArray;
    }

    private static String findType(Field field, SyncanoField fieldAnnotation) {
        String typeName = fieldAnnotation.type();
        if (typeName != null && !typeName.isEmpty()) {
            return typeName;
        }

        Class<?> type = field.getType();
        if (type.equals(int.class) || type.equals(Integer.class)
                || type.equals(byte.class) || type.equals(Byte.class)
                || type.equals(short.class) || type.equals(Short.class)) {
            return Constants.FIELD_TYPE_INTEGER;
        } else if (type.equals(String.class)) {
            return Constants.FIELD_TYPE_STRING;
        } else if (type.equals(Date.class)) {
            return Constants.FIELD_TYPE_DATETIME;
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Constants.FIELD_TYPE_BOOLEAN;
        } else if (type.equals(float.class) || type.equals(Float.class)
                || type.equals(double.class) || type.equals(Double.class)) {
            return Constants.FIELD_TYPE_FLOAT;
        }
        if (fieldAnnotation.readOnly()) {
            return null;
        }
        throw new RuntimeException("Field type " + type.getSimpleName() + " is not supported.");
    }
}
