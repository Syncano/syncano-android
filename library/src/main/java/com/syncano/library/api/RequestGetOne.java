package com.syncano.library.api;

import com.syncano.library.Syncano;

public class RequestGetOne<T> extends RequestGet<T> {

    protected Class<T> resultType;

    public RequestGetOne(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }

    @Override
    public T parseResult(String json) {
        return gson.fromJson(json, resultType);
    }
}
