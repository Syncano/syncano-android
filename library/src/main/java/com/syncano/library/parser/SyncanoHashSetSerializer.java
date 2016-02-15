package com.syncano.library.parser;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.syncano.library.utils.SyncanoHashSet;

import java.lang.reflect.Type;

class SyncanoHashSetSerializer implements JsonSerializer<SyncanoHashSet> {
    public JsonElement serialize(SyncanoHashSet syncanoHashSet, Type type, JsonSerializationContext jsc) {
        return new JsonPrimitive(syncanoHashSet.getAsString());
    }
}
