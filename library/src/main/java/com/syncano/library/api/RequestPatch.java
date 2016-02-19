package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestPatch<T> extends SendRequest<T> {

    public RequestPatch(Class<T> responseType, String url, Syncano syncano, Object data) {
        super(responseType, url, syncano, data);
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_PATCH;
    }


}
