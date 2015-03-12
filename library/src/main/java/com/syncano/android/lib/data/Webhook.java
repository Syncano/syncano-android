package com.syncano.android.lib.data;


import com.syncano.android.lib.annotation.SyncanoField;

public class Webhook {

    @SyncanoField(name = "slug", readOnly = true, required = true)
    private String slug;

    @SyncanoField(name = "codebox", required = true)
    private int codebox;

    @SyncanoField(name = "public_link", readOnly = true)
    private String publicLink;

    @SyncanoField(name = "public")
    private boolean isPublic;

    public Webhook() {
    }

    public Webhook(String slug, int codebox) {
        this.slug = slug;
        this.codebox = codebox;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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
}
