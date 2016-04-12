package com.syncano.library.data;

@Deprecated
public class Webhook extends ScriptEndpoint {

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
        Links links = super.getLinks();
        if (links != null) {
            WebHookLinks webHookLinks = new WebHookLinks(links);
            webHookLinks.codebox = links.script;
            return webHookLinks;
        }
        return null;
    }

    @Deprecated
    public void setLinks(WebHookLinks links) {
        if (links == null) {
            super.setLinks(null);
        } else {
            if (links.script != null) {
                links.codebox = links.script;
            } else if (links.codebox != null) {
                links.script = links.codebox;
            }
            super.setLinks(links);
        }
    }

    @Deprecated
    public static class WebHookLinks extends Links {
        public String codebox;

        public WebHookLinks() {
        }

        public WebHookLinks(Links l) {
            super(l);
        }
    }
}