package com.syncano.library.parser;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.NanosDate;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoHashSet;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;

public class GsonParser {

    public static Gson createGson() {
        return createGson(new GsonParseConfig());
    }

    public static <T> Gson createGson(GsonParseConfig config) {
        return createGson(null, config);
    }

    public static <T> Gson createGson(T object) {
        return createGson(object, new GsonParseConfig());
    }

    public static <T> Gson createGson(final T object, GsonParseConfig config) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoHashSet.class, new SyncanoHashSetDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoHashSet.class, new SyncanoHashSetSerializer());
        gsonBuilder.registerTypeAdapter(SyncanoFile.class, new FileDeserializer());
        gsonBuilder.addSerializationExclusionStrategy(new SyncanoSerializationStrategy(config.readOnlyNotImportant));
        gsonBuilder.addDeserializationExclusionStrategy(new SyncanoDeserializationStrategy());
        gsonBuilder.setFieldNamingStrategy(new SyncanoFieldNamingStrategy());
        gsonBuilder.registerTypeAdapterFactory(new SyncanoObjectAdapterFactory(SyncanoObject.class));

        // it makes possible to fill existing object instead of creating new one
        if (object != null) {
            gsonBuilder.registerTypeAdapter(object.getClass(), new InstanceCreator<T>() {
                @Override
                public T createInstance(Type type) {
                    return object;
                }
            });
        }
        return gsonBuilder.create();
    }

    public static class GsonParseConfig {
        public boolean readOnlyNotImportant = false;
    }

    private static class SyncanoFieldNamingStrategy implements FieldNamingStrategy {

        @Override
        public String translateName(Field f) {
            return SyncanoClassHelper.getFieldName(f);
        }
    }

}
