package com.syncano.library.data;

import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoField;

public class PushMessage {
    @SyncanoField(name = "content")
    private JsonObject content;

    public JsonObject getContent() {
        return content;
    }

    public void setContent(JsonObject content) {
        this.content = content;
    }
}
