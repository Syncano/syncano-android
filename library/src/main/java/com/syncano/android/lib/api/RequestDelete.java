package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.utils.SyncanoHttpClient;

public class RequestDelete<T> extends Request <T> {

    public RequestDelete(Class<T> responseType, String url, Syncano syncano) {
        super(responseType, url, syncano);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_DELETE;
    }

    @Override
    public String prepareParams() {
        return null;
    }
}
