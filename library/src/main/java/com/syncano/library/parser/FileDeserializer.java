package com.syncano.library.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.syncano.library.data.SyncanoFile;

import java.lang.reflect.Type;

class FileDeserializer implements JsonDeserializer<SyncanoFile> {
    public SyncanoFile deserialize(JsonElement json, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        String link = json.getAsJsonObject().get("value").getAsString();
        return new SyncanoFile(link);
    }
}
