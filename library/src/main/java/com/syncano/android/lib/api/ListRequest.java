package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;

import java.util.List;

public abstract class ListRequest<T> extends Request<List<T>> {

    protected Class<T> resultType;

    protected ListRequest(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }
}