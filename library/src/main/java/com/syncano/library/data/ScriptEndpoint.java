package com.syncano.library.data;


import com.google.gson.JsonObject;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.utils.SyncanoLog;

public class ScriptEndpoint {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_SCRIPT = "script";
    public static final String FIELD_PUBLIC = "public";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LINKS = "links";

    @SyncanoField(name = FIELD_NAME, readOnly = true, required = true)
    private String name;

    @SyncanoField(name = FIELD_SCRIPT, required = true)
    private int script;

    @SyncanoField(name = FIELD_PUBLIC)
    private boolean isPublic = false;

    @SyncanoField(name = FIELD_DESCRIPTION)
    private String description;

    @SyncanoField(name = FIELD_LINKS, readOnly = true)
    private Links links;

    private Trace trace;
    private Object customResponse;
    private Syncano syncano;

    public ScriptEndpoint() {
    }

    public ScriptEndpoint(String name) {
        this.name = name;
    }

    public ScriptEndpoint(String name, int script) {
        this.name = name;
        this.script = script;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @deprecated "use getScript()"
     */
    @Deprecated
    public int getCodebox() {
        return getScript();
    }

    public int getScript() {
        return script;
    }

    /**
     * @deprecated "use setScript()"
     */
    @Deprecated
    public void setCodebox(int codebox) {
        setScript(codebox);
    }

    public void setScript(int script) {
        this.script = script;
    }

    public String getPublicLink() {
        if (links == null || links.publicLink == null) {
            return null;
        }
        if (getSyncano() != null) {
            return getSyncano().getUrl() + links.publicLink;
        }
        return Constants.PRODUCTION_SERVER_URL + links.publicLink;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Links getLinks() {
        return links;
    }

    public void setLinks(Links links) {
        this.links = links;
    }

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public ScriptEndpoint on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public String getOutput() {
        if (trace != null) {
            return trace.getOutput();
        }
        SyncanoLog.d(ScriptEndpoint.class.getSimpleName(), "Getting output, without calling run() first");
        return null;
    }

    public String getErrorOutput() {
        if (trace != null) {
            return trace.getErrorOutput();
        }
        SyncanoLog.d(ScriptEndpoint.class.getSimpleName(), "Getting output, without calling run() first");
        return null;
    }

    public Response<Trace> run() {
        return getSyncano().runScriptEndpoint(this).send();
    }

    public Response<Trace> run(JsonObject payload) {
        return getSyncano().runScriptEndpoint(this, payload).send();
    }

    public void run(SyncanoCallback<Trace> callback) {
        getSyncano().runScriptEndpoint(this).sendAsync(callback);
    }

    public void run(JsonObject payload, SyncanoCallback<Trace> callback) {
        getSyncano().runScriptEndpoint(this, payload).sendAsync(callback);
    }

    public Response<String> runCustomResponse() {
        return getSyncano().runScriptEndpointCustomResponse(this).send();
    }

    public <T> Response<T> runCustomResponse(Class<T> type) {
        return getSyncano().runScriptEndpointCustomResponse(this, type).send();
    }

    public Response<String> runCustomResponse(JsonObject payload) {
        return getSyncano().runScriptEndpointCustomResponse(this, payload).send();
    }

    public <T> Response<T> runCustomResponse(Class<T> type, JsonObject payload) {
        return getSyncano().runScriptEndpointCustomResponse(this, type, payload).send();
    }

    public void runCustomResponse(SyncanoCallback<String> callback) {
        getSyncano().runScriptEndpointCustomResponse(this).sendAsync(callback);
    }

    public <T> void runCustomResponse(Class<T> type, SyncanoCallback<T> callback) {
        getSyncano().runScriptEndpointCustomResponse(this, type).sendAsync(callback);
    }

    public void runCustomResponse(JsonObject payload, SyncanoCallback<String> callback) {
        getSyncano().runScriptEndpointCustomResponse(this, payload).sendAsync(callback);
    }

    public <T> void runCustomResponse(Class<T> type, JsonObject payload, SyncanoCallback<T> callback) {
        getSyncano().runScriptEndpointCustomResponse(this, type, payload).sendAsync(callback);
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public <T> T getCustomResponse() {
        return (T) customResponse;
    }

    public void setCustomResponse(Object customResponse) {
        this.customResponse = customResponse;
    }

    public static class Links {
        public Links() {
        }

        public Links(Links l) {
            resetLink = l.resetLink;
            script = l.script;
            run = l.run;
            publicLink = l.publicLink;
            self = l.self;
            traces = l.traces;
        }

        @SyncanoField(name = "reset-link")
        public String resetLink;
        @SyncanoField(name = "script")
        public String script;
        @SyncanoField(name = "run")
        public String run;
        @SyncanoField(name = "public-link")
        public String publicLink;
        @SyncanoField(name = "self")
        public String self;
        @SyncanoField(name = "traces")
        public String traces;
    }
}