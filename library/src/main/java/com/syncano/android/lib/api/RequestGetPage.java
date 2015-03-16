package com.syncano.android.lib.api;


import com.syncano.android.lib.Syncano;

public class RequestGetPage<T> extends RequestGet <T> {

    public RequestGetPage(Class<T> responseType, String url, Syncano syncano) {
        super(responseType, url, syncano);
    }

    @Override
    public T parseResult(String json) {
        return super.parseResult(json);
    }
}
