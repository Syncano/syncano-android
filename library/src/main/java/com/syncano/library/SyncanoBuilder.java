package com.syncano.library;

import android.content.Context;

public class SyncanoBuilder {

    private String instanceName = null;
    private String apiKey = null;
    private String customServerUrl = null;
    private boolean rememberLoggedUser = true;
    private boolean strictCheckCertificate = false;
    private Context androidContext = null;
    private boolean global = true;

    public SyncanoBuilder instanceName(String instanceName) {
        this.instanceName = instanceName;
        return this;
    }

    public SyncanoBuilder apiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public SyncanoBuilder customServerUrl(String customServerUrl) {
        this.customServerUrl = customServerUrl;
        return this;
    }

    public SyncanoBuilder useLoggedUserStorage(boolean rememberUser) {
        this.rememberLoggedUser = rememberUser;
        return this;
    }

    public SyncanoBuilder strictCheckCertificate(boolean strictCheckCertificate) {
        this.strictCheckCertificate = strictCheckCertificate;
        return this;
    }

    public SyncanoBuilder androidContext(Context androidContext) {
        this.androidContext = androidContext;
        return this;
    }

    public SyncanoBuilder globalInstance(boolean global) {
        this.global = global;
        return this;
    }

    public Syncano build() {
        Syncano s = new Syncano();
        s.set
        return s;
    }
}
