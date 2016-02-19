package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestPost<T> extends SendRequest<T> {

    public RequestPost(Class<T> responseType, String url, Syncano syncano, Object data) {
        super(responseType, url, syncano, data);
        addCorrectHttpResponseCode(Response.HTTP_CODE_CREATED);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_POST;
    }
}
