package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public class RequestPost<T> extends SimpleRequest<T> {

    private Object data;

    public RequestPost(Class<T> responseType, String url, Syncano syncano, Object data) {
        super(responseType, url, syncano);
        this.data = data;
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_POST;
    }

    @Override
    public String prepareParams() {

        if (data != null) {
            return gson.toJson(data);
        }

        return null;
    }
}
