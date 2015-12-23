package com.syncano.library.data;


import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.utils.SyncanoLog;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;

public class Webhook {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODEBOX = "codebox";
    public static final String FIELD_PUBLIC_LINK = "public_link";
    public static final String FIELD_PUBLIC = "public";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_LINKS = "links";

    @SyncanoField(name = FIELD_NAME, readOnly = true, required = true)
    private String name;

    @SyncanoField(name = FIELD_CODEBOX, required = true)
    private int codebox;

    @SyncanoField(name = FIELD_PUBLIC_LINK, readOnly = true)
    private String publicLink;

    @SyncanoField(name = FIELD_PUBLIC)
    private boolean isPublic;

    @SyncanoField(name = FIELD_DESCRIPTION)
    private String description;

    @SyncanoField(name = FIELD_LINKS, readOnly = true)
    private WebHookLinks links;

    private Trace trace;
    private Syncano syncano;

    public Webhook() {
    }

    public Webhook(String name) {
        this.name = name;
    }

    public Webhook(String name, int codebox) {
        this.name = name;
        this.codebox = codebox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCodebox() {
        return codebox;
    }

    public void setCodebox(int codebox) {
        this.codebox = codebox;
    }

    public String getPublicLink() {
        return publicLink;
    }

    public void setPublicLink(String publicLink) {
        this.publicLink = publicLink;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WebHookLinks getLinks() {
        return links;
    }

    public void setLinks(WebHookLinks links) {
        this.links = links;
    }

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public Webhook on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public String getOutput() {
        if (trace != null) {
            return trace.getOutput();
        }
        SyncanoLog.d(Webhook.class.getSimpleName(), "Getting output, without calling run() first");
        return null;
    }

    public String getErrorOutput() {
        if (trace != null) {
            return trace.getErrorOutput();
        }
        SyncanoLog.d(Webhook.class.getSimpleName(), "Getting output, without calling run() first");
        return null;
    }

    public Response<Trace> run() {
        return getSyncano().runWebhook(this).send();
    }

    public Response<Trace> run(JsonObject payload) {
        return getSyncano().runWebhook(this, payload).send();
    }

    public void run(SyncanoCallback<Trace> callback) {
        getSyncano().runWebhook(this).sendAsync(callback);
    }

    public void run(SyncanoCallback<Trace> callback, JsonObject payload) {
        getSyncano().runWebhook(this, payload).sendAsync(callback);
    }

    public Trace getTrace() {
        return trace;
    }

    public void setTrace(Trace trace) {
        this.trace = trace;
    }

    public static class WebHookLinks {
        @SyncanoField(name = "reset-link")
        public String resetLink;
        @SyncanoField(name = "codebox")
        public String codebox;
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
