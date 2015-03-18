package com.syncano.android.lib.data;


import com.syncano.android.lib.annotation.SyncanoField;

public class Webhook {

    public static final String FIELD_SLUG = "slug";
    public static final String FIELD_CODEBOX = "codebox";
    public static final String FIELD_PUBLIC_LINK = "public_link";
    public static final String FIELD_PUBLIC = "public";

    @SyncanoField(name = FIELD_SLUG, readOnly = true, required = true)
    private String slug;

    @SyncanoField(name = FIELD_CODEBOX, required = true)
    private int codebox;

    @SyncanoField(name = FIELD_PUBLIC_LINK, readOnly = true)
    private String publicLink;

    @SyncanoField(name = FIELD_PUBLIC)
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
