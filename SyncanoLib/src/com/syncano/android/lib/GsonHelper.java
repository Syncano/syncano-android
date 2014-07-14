package com.syncano.android.lib;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.utils.DateTool;

public class GsonHelper {

	public static Gson createGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapterFactory(new ParamsTypeAdapterFactory());
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

	private static class ParamsTypeAdapterFactory extends CustomizedTypeAdapterFactory<Params> {
		private ParamsTypeAdapterFactory() {
			super(Params.class);
		}

		@Override
		protected void beforeWrite(Params source, JsonElement toSerialize) {
			HashMap<String, String> other = source.getAdditionalParams();
			if (other != null) {
				JsonObject job = toSerialize.getAsJsonObject();
				Set<String> keys = other.keySet();
				for (String key : keys) {
					job.addProperty(key, other.get(key));
				}
			}
		}

		@Override
		protected void afterRead(JsonElement deserialized) {
		}
	}

	/**
	 * Makes easier to read its subclasses
	 */
	private static abstract class CustomizedTypeAdapterFactory<C> implements TypeAdapterFactory {
		private final Class<C> customizedClass;

		public CustomizedTypeAdapterFactory(Class<C> customizedClass) {
			this.customizedClass = customizedClass;
		}

		@SuppressWarnings("unchecked")
		// we use a runtime check to guarantee that 'C' and 'T' are equal
		public final <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
			if (customizedClass.isAssignableFrom(type.getRawType())) {
				return (TypeAdapter<T>) customizeMyClassAdapter(gson, (TypeToken<C>) type);
			}
			return null;
		}

		private TypeAdapter<C> customizeMyClassAdapter(Gson gson, TypeToken<C> type) {
			final TypeAdapter<C> delegate = gson.getDelegateAdapter(this, type);
			final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
			return new TypeAdapter<C>() {
				@Override
				public void write(JsonWriter out, C value) throws IOException {
					JsonElement tree = delegate.toJsonTree(value);
					beforeWrite(value, tree);
					elementAdapter.write(out, tree);
				}

				@Override
				public C read(JsonReader in) throws IOException {
					JsonElement tree = elementAdapter.read(in);
					afterRead(tree);
					return delegate.fromJsonTree(tree);
				}
			};
		}

		/**
		 * Override this to muck with {@code toSerialize} before it is written to the outgoing JSON stream.
		 */
		protected void beforeWrite(C source, JsonElement toSerialize) {
		}

		/**
		 * Override this to muck with {@code deserialized} before it parsed into the application type.
		 */
		protected void afterRead(JsonElement deserialized) {
		}
	}
}
