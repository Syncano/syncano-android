package com.syncano.android.lib.data;


import com.syncano.android.lib.annotation.SyncanoField;

public class Webhook {

    @SyncanoField(name = "slug", readOnly = true)
    private String slug;

    @SyncanoField(name = "codebox")
    private int codebox;

    @SyncanoField(name = "public_link", readOnly = true)
    private String publicLink;

    @SyncanoField(name = "public")
    private boolean isPublic;

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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
