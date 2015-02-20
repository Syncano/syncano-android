package com.syncano.android.lib.utils;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonHelper {

	public static Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
		gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
		return gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
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
}
