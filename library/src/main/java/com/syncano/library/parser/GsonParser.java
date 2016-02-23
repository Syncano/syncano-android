package com.syncano.library.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.NanosDate;
import com.syncano.library.utils.SyncanoHashSet;

import java.lang.reflect.Type;
import java.util.Date;

public class GsonParser {

    public static <T> Gson createGson(T object) {
        return createGson(object, null);
    }

    public static <T> Gson createGson(final T object, GsonParseConfig config) {
        if (config == null) config = new GsonParseConfig();
        if (object instanceof SyncanoObject) {
            return createSyncanoObjectGsonBuilder(object, config).create();
        }
        GsonBuilder gsonBuilder = getDefaultGsonBuilder(config);
        gsonBuilder.registerTypeAdapter(object.getClass(), new InstanceCreator<T>() {
            @Override
            public T createInstance(Type type) {
                return object;
            }
        });
        return gsonBuilder.create();
    }

    public static Gson createGson(Class objectType) {
        return createGson(objectType, null);
    }

    public static Gson createGson(Class objectType, GsonParseConfig config) {
        if (config == null) config = new GsonParseConfig();
        if (SyncanoObject.class.isAssignableFrom(objectType)) {
            return createSyncanoObjectGsonBuilder(null, config).create();
        }
        return getDefaultGsonBuilder(config).create();
    }

    private static <T> GsonBuilder createSyncanoObjectGsonBuilder(T object, GsonParseConfig config) {
        GsonBuilder gsonBuilder = getDefaultGsonBuilder(config);
        gsonBuilder.registerTypeHierarchyAdapter(SyncanoObject.class, new SyncanoObjectDeserializer(object));
        gsonBuilder.registerTypeHierarchyAdapter(SyncanoObject.class, new SyncanoObjectSerializer(config.serializeReadOnlyFields));
        gsonBuilder.serializeNulls();
        return gsonBuilder;
    }

    private static <T> GsonBuilder getDefaultGsonBuilder(GsonParseConfig config) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(NanosDate.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoHashSet.class, new SyncanoHashSetDeserializer());
        gsonBuilder.registerTypeAdapter(SyncanoHashSet.class, new SyncanoHashSetSerializer());
        gsonBuilder.registerTypeAdapter(SyncanoFile.class, new FileDeserializer());
        gsonBuilder.setFieldNamingStrategy(new SyncanoFieldNamingStrategy());
        gsonBuilder.addSerializationExclusionStrategy(
                new SyncanoExclusionStrategy(config.serializeReadOnlyFields));
        return gsonBuilder;
    }

    public static class GsonParseConfig {
        public boolean serializeReadOnlyFields = false;
    }

}
