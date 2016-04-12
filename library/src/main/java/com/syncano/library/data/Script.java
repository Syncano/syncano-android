package com.syncano.library.data;

import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.RuntimeName;

public class Script extends Entity {

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
    private JsonObject config;

    private Trace trace;
    private Syncano syncano;

    public Script() {
    }

    public Script(int id) {
        setId(id);
    }

    public Script(String label, String source, RuntimeName runtimeName) {
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

    public JsonObject getConfig() {
        return config;
    }

    public void setConfig(JsonObject config) {
        this.config = config;
    }

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public Script on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public Response<Trace> run() {
        return getSyncano().runScript(this).send();
    }

    public Response<Trace> run(JsonObject payload) {
        return getSyncano().runScript(this, payload).send();
    }

    public void run(SyncanoCallback<Trace> callback) {
        getSyncano().runScript(this).sendAsync(callback);
    }

    public void run(SyncanoCallback<Trace> callback, JsonObject payload) {
        getSyncano().runScript(this, payload).sendAsync(callback);
    }
}
