package com.syncano.library.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.utils.DateTool;

import java.lang.reflect.Type;
import java.util.Date;

class DateSerializer implements JsonSerializer<Date> {
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsc) {
        return new JsonPrimitive(DateTool.parseDate(date));
    }
}
