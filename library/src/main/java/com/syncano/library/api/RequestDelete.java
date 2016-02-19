package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestDelete<T> extends ResultRequest<T> {

    public RequestDelete(Class<T> responseType, String url, Syncano syncano) {
        super(responseType, url, syncano);
        addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_DELETE;
    }
}
