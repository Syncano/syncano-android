package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestDelete<T> extends ResultRequest<T> {

    private Integer id;

    public RequestDelete(Class<T> responseType, String url, Syncano syncano, Integer id) {
        super(responseType, url, syncano);
        addCorrectHttpResponseCode(Response.HTTP_CODE_NOT_FOUND);
        addCorrectHttpResponseCode(Response.HTTP_CODE_NO_CONTENT);
        this.id = id;
    }

    public RequestDelete(Class<T> responseType, String url, Syncano syncano) {
        this(responseType, url, syncano, null);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_DELETE;
    }

    public Integer getId() {
        return id;
    }
}
