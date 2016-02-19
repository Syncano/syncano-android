package com.syncano.library.data;

import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoField;

public class PushMessage {
    @SyncanoField(name = "content")
    public JsonObject content;
}
