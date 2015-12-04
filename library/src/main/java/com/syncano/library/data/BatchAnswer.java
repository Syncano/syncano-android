package com.syncano.library.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.utils.GsonHelper;

public class BatchAnswer {
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_CODE = "code";

    @SyncanoField(name = FIELD_CONTENT)
    private JsonObject content;
    @SyncanoField(name = FIELD_CODE)
    private int code;

    private Gson gson;

    public <T> T getDataAs(Class<T> type) {
        if (content == null) {
            return null;
        }
        return GsonHelper.createGson().fromJson(content, type);
    }
}
