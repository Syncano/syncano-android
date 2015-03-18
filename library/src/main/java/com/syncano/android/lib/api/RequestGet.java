package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.utils.SyncanoHttpClient;

public class RequestGet<T> extends SimpleRequest<T> {

    public RequestGet(Class<T> responseType, String url, Syncano syncano) {
        super(responseType, url, syncano);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_GET;
    }

    @Override
    public String prepareParams() {
        return null;
    }
}
