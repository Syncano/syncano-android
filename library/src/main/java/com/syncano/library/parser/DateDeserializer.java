package com.syncano.library.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.syncano.library.utils.DateTool;

import java.lang.reflect.Type;
import java.util.Date;

class DateDeserializer implements JsonDeserializer<Date> {
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        String dateString;
        if (json.isJsonPrimitive()) {
            dateString = json.getAsJsonPrimitive().getAsString();
        } else {
            dateString = json.getAsJsonObject().get("value").getAsString();
        }
        return DateTool.parseString(dateString);
    }
}
