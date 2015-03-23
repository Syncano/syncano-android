package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestDelete<T> extends SimpleRequest<T> {

    public RequestDelete(Class<T> responseType, String url, Syncano syncano) {
        super(responseType, url, syncano);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_DELETE;
    }
}
