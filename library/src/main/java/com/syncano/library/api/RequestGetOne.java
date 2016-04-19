package com.syncano.library.api;

import com.syncano.library.Syncano;

@Deprecated
public class RequestGetOne<T> extends RequestGet<T> {
    @Deprecated
    public RequestGetOne(Class<T> resultType, String url, Syncano syncano) {
        super(resultType, url, syncano);
    }

    @Deprecated
    public RequestGetOne(T dataObject, String url, Syncano syncano) {
        super(dataObject, url, syncano);
    }
}
