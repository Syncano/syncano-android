package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

@Deprecated
public class Webhook extends ScriptEndpoint {

    @SyncanoField(name = FIELD_LINKS, readOnly = true)
    private WebHookLinks links;

    @Deprecated
    public Webhook() {
    }

    @Deprecated
    public Webhook(String name) {
        super(name);
    }

    @Deprecated
    public Webhook(String name, int script) {
        super(name, script);
    }

    @Deprecated
    public WebHookLinks getLinks() {
        return links;
    }

    @Deprecated
    public void setLinks(WebHookLinks links) {
        this.links = links;
    }

    @Deprecated
    public static class WebHookLinks extends ScriptEndpoint.Links {
    }
}