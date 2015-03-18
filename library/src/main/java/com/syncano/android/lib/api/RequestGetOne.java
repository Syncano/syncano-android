package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.utils.SyncanoHttpClient;

import java.util.HashMap;
import java.util.Map;

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
