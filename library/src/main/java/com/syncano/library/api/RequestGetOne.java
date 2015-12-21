package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.GsonHelper;

public class RequestGetOne<T> extends RequestGet<T> {

    protected Class<T> resultType;

    public RequestGetOne(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }

    public RequestGetOne(T dataObject, String url, Syncano syncano) {
        super(url, syncano);
        gson = GsonHelper.createGson(dataObject);
        this.resultType = (Class<T>) dataObject.getClass();
    }

    @Override
    public T parseResult(Response<T> response, String json) {
        return gson.fromJson(json, resultType);
    }
}
