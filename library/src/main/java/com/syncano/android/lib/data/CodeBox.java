package com.syncano.android.lib.data;

import com.syncano.android.lib.annotation.SyncanoField;
import com.syncano.android.lib.choice.RuntimeName;

import org.json.JSONObject;

public class CodeBox extends Entity {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_RUNTIME_NAME = "runtime_name";
    public static final String FIELD_CONFIG = "config";

    @SyncanoField(name = FIELD_NAME, required = true)
    private String name;

    @SyncanoField(name = FIELD_DESCRIPTION)
    private String description;

    @SyncanoField(name = FIELD_SOURCE, required = true)
    private String source;

    @SyncanoField(name = FIELD_RUNTIME_NAME, required = true)
    private RuntimeName runtimeName;

    @SyncanoField(name = FIELD_CONFIG)
    private JSONObject config;

    public CodeBox() {
    }

    public CodeBox(String name, String source, RuntimeName runtimeName) {
        this.name = name;
        this.source = source;
        this.runtimeName = runtimeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public RuntimeName getRuntimeName() {
        return runtimeName;
    }

    public void setRuntimeName(RuntimeName runtimeName) {
        this.runtimeName = runtimeName;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }
}
