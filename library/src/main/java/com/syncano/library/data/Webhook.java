package com.syncano.library.data;


import com.syncano.library.annotation.SyncanoField;

public class Webhook {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODEBOX = "codebox";
    public static final String FIELD_PUBLIC_LINK = "public_link";
    public static final String FIELD_PUBLIC = "public";

    @SyncanoField(name = FIELD_NAME, readOnly = true, required = true)
    private String name;

    @SyncanoField(name = FIELD_CODEBOX, required = true)
    private int codebox;

    @SyncanoField(name = FIELD_PUBLIC_LINK, readOnly = true)
    private String publicLink;

    @SyncanoField(name = FIELD_PUBLIC)
    private boolean isPublic;

    public Webhook() {
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
}
