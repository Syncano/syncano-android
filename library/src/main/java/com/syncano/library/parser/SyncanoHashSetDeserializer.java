package com.syncano.library.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.syncano.library.utils.SyncanoHashSet;

import java.lang.reflect.Type;
import java.util.Collections;

class SyncanoHashSetDeserializer implements JsonDeserializer<SyncanoHashSet> {
    public SyncanoHashSet deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        SyncanoHashSet syncanoHashSet = new SyncanoHashSet();
        String[] spitedString = json.getAsJsonPrimitive().getAsString().split(",");
        Collections.addAll(syncanoHashSet, spitedString);
        return syncanoHashSet;
    }
}
