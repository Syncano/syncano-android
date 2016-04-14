package com.syncano.library.api;

import com.google.gson.Gson;
import com.syncano.library.Syncano;
import com.syncano.library.parser.GsonParser;

public abstract class ResultRequest<T> extends HttpRequest<T> {

    protected Class<T> resultType = null;
    private Gson gson = null;

    protected ResultRequest(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        gson = GsonParser.createGson(resultType);
        this.resultType = resultType;
    }

    protected ResultRequest(T dataObject, String url, Syncano syncano) {
        super(url, syncano);
        gson = GsonParser.createGson(dataObject);
        this.resultType = (Class<T>) dataObject.getClass();
    }

    protected ResultRequest(String url, Syncano syncano) {
        super(url, syncano);
    }

    @Override
    public T parseResult(Response<T> response, String json) {
        if (resultType.equals(String.class)) {
            return (T) json;
        }
        return gson.fromJson(json, resultType);
    }

    public Class getResultType() {
        return resultType;
    }
}
