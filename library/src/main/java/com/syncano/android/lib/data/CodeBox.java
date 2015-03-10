package com.syncano.android.lib.data;

import com.syncano.android.lib.annotation.SyncanoField;
import com.syncano.android.lib.choice.RuntimeName;

import org.json.JSONObject;

public class CodeBox extends Entity {

    @SyncanoField(name = "name")
    private String name;

    @SyncanoField(name = "description")
    private String description;

    @SyncanoField(name = "source")
    private String source;

    @SyncanoField(name = "runtime_name")
    private RuntimeName runtimeName;

    @SyncanoField(name = "config")
    private JSONObject config;

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
