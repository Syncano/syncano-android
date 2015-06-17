package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.RuntimeName;

import org.json.JSONObject;

public class CodeBox extends Entity {

    public static final String FIELD_LABEL = "label";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SOURCE = "source";
    public static final String FIELD_RUNTIME_NAME = "runtime_name";
    public static final String FIELD_CONFIG = "config";

    @SyncanoField(name = FIELD_LABEL, required = true)
    private String label;

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

    public CodeBox(String label, String source, RuntimeName runtimeName) {
        this.label = label;
        this.source = source;
        this.runtimeName = runtimeName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
