package com.syncano.library.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.syncano.library.Constants;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
        Collection<Field> fields = findAllSyncanoFields(clazz);
        for (Field field : fields) {
            SyncanoField fieldAnnotation = field.getAnnotation(SyncanoField.class);
            if (fieldAnnotation == null || !fieldAnnotation.inSchema() || fieldAnnotation.onlyLocal()) {
                continue;
            }

            JsonObject fieldDescription = new JsonObject();
            FieldType type = findType(field);
            if (type == null) {
                continue;
            }

            if (type.equals(FieldType.REFERENCE) && SyncanoObject.class.isAssignableFrom(field.getType())) {
                String target = fieldAnnotation.target();
                if (target == null || target.isEmpty()) {
                    if (field.getType().equals(field.getDeclaringClass())) {
                        target = "self";
                    } else {
                        target = SyncanoClassHelper.getSyncanoClassName((Class<? extends SyncanoObject>) field.getType());
                    }
                }
                fieldDescription.addProperty(Constants.FIELD_TARGET, target);
            }
            String typeName;
            try {
                SerializedName nameAnn = type.getClass().getDeclaredField(type.name()).getAnnotation(SerializedName.class);
                typeName = nameAnn.value();
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            fieldDescription.addProperty(Constants.FIELD_TYPE, typeName);
            fieldDescription.addProperty(Constants.FIELD_NAME, getFieldName(field, true));
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

    public static String getOfflineFieldName(Field f) {
        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);
        String offlineName = syncanoField.offlineName();
        if (!offlineName.isEmpty()) {
            return offlineName;
        }
        return getFieldName(f);
    }

    public static String getFieldName(Field f) {
        return getFieldName(f, false);
    }

    public static String getFieldName(Field f, boolean checkCapitalLetters) {
        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);

        String name;
        if (syncanoField != null) {
            name = syncanoField.name();
        } else {
            name = f.getName();
        }
        if (checkCapitalLetters && !name.equals(name.toLowerCase())) {
            throw new RuntimeException("Can't use capital letters in field names");
        }
        return name;
    }

    public static FieldType findType(Field field) {
        SyncanoField fieldAnnotation = field.getAnnotation(SyncanoField.class);
        FieldType type = fieldAnnotation.type();
        if (type != null && !FieldType.NOT_SET.equals(type)) {
            return type;
        }

        Class<?> clazz = field.getType();
        if (clazz.equals(int.class) || clazz.equals(Integer.class)
                || clazz.equals(byte.class) || clazz.equals(Byte.class)
                || clazz.equals(short.class) || clazz.equals(Short.class)) {
            return FieldType.INTEGER;
        } else if (clazz.equals(String.class) || clazz.isEnum()) {
            return FieldType.STRING;
        } else if (clazz.equals(Date.class) || clazz.equals(NanosDate.class)) {
            return FieldType.DATETIME;
        } else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
            return FieldType.BOOLEAN;
        } else if (SyncanoObject.class.isAssignableFrom(clazz)) {
            return FieldType.REFERENCE;
        } else if (clazz.equals(float.class) || clazz.equals(Float.class)
                || clazz.equals(double.class) || clazz.equals(Double.class)) {
            return FieldType.FLOAT;
        } else if (clazz.equals(SyncanoFile.class)) {
            return FieldType.FILE;
        }
        return null;
    }

    public static Collection<Field> findAllSyncanoFields(Class type) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }

        ArrayList<Field> syncanoFields = new ArrayList<>();
        for (Field field : fields) {
            SyncanoField fieldAnnotation = field.getAnnotation(SyncanoField.class);
            if (fieldAnnotation == null) {
                continue;
            }
            syncanoFields.add(field);
        }
        return syncanoFields;
    }
}
