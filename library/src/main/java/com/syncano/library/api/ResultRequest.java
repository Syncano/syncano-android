package com.syncano.library.api;

import com.syncano.library.Syncano;

public abstract class ResultRequest<T> extends HttpRequest<T> {

    protected Class<T> resultType;

    protected ResultRequest(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }

    @Override
    public T parseResult(Response<T> response, String json) {
        if (resultType.equals(String.class)) {
            return (T) json;
        }
        return gson.fromJson(json, resultType);
    }
}
