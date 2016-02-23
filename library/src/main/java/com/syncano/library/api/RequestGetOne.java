package com.syncano.library.api;

import com.google.gson.Gson;
import com.syncano.library.Syncano;
import com.syncano.library.parser.GsonParser;

public class RequestGetOne<T> extends RequestGet<T> {

    protected Class<T> resultType;
    private Gson gson;

    public RequestGetOne(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
        gson = GsonParser.createGson(resultType);
    }

    public RequestGetOne(T dataObject, String url, Syncano syncano) {
        super(url, syncano);
        gson = GsonParser.createGson(dataObject);
        this.resultType = (Class<T>) dataObject.getClass();
    }

    @Override
    public T parseResult(Response<T> response, String json) {
        return gson.fromJson(json, resultType);
    }
}
