package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;

public abstract class SimpleRequest<T> extends Request<T> {

    protected Class<T> resultType;

    protected SimpleRequest(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }

    @Override
    public T parseResult(String json) {
        return gson.fromJson(json, resultType);
    }
}
