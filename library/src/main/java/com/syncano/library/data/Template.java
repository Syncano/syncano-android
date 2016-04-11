package com.syncano.library.data;

import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoField;

public class Template {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_CONTENT = "content";
    public static final String FIELD_CONTENT_TYPE = "content_type";
    public static final String FIELD_CONTEXT = "context";

    @SyncanoField(name = FIELD_NAME, required = true)
    private String name;
    @SyncanoField(name = FIELD_CONTENT, required = true)
    private String content;
    @SyncanoField(name = FIELD_CONTENT_TYPE)
    private String contentType;
    @SyncanoField(name = FIELD_CONTEXT)
    private JsonObject context;

    public Template() {
    }

    public Template(String name) {
        this.name = name;
    }


}
