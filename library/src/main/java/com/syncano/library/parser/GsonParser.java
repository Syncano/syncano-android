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

    public static Gson createGson() {
        return createGson(new GsonParseConfig());
    }

    public static Gson createGson(GsonParseConfig config) {
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
        gsonBuilder.setFieldNamingStrategy(new SyncanoFieldNamingStrategy());
        gsonBuilder.addSerializationExclusionStrategy(new SyncanoExclusionStrategy(config.serializeReadOnlyFields));
        gsonBuilder.registerTypeHierarchyAdapter(SyncanoObject.class, new SyncanoObjectDeserializer(object));
        gsonBuilder.registerTypeHierarchyAdapter(SyncanoObject.class, new SyncanoObjectSerializer(config.serializeReadOnlyFields));
        if (object != null && !(object instanceof SyncanoObject)) {
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
        public boolean serializeReadOnlyFields = false;
    }

}
