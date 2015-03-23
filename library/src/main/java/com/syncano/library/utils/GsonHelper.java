package com.syncano.library.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.annotation.SyncanoField;

public class GsonHelper {

    public static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.addSerializationExclusionStrategy(new SyncanoSerializationStrategy());
        gsonBuilder.setFieldNamingStrategy(new SyncanoFieldNamingStrategy());
        return gsonBuilder.create();
    }

    private static class DateSerializer implements JsonSerializer<Date> {
        public JsonElement serialize(Date date, Type type, JsonSerializationContext jsc) {
            return new JsonPrimitive(DateTool.parseDate(date));
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {
        public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
            return DateTool.parseString(json.getAsJsonPrimitive().getAsString());
        }
    }

    private static class SyncanoSerializationStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {

            SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);

            // Don't serialize read only fields (like "id" or "created_at").
            // We want only to receive it, not send.
            if (syncanoField == null || (syncanoField.readOnly() == true && syncanoField.required() == false )) {
                return true;
            }

            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

    private static class SyncanoFieldNamingStrategy implements FieldNamingStrategy {

        @Override
        public String translateName(Field f) {
            SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);

            if (syncanoField != null) {
                return syncanoField.name();
            } else {
                return f.getName();
            }
        }
    }
}
