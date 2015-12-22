package com.syncano.library.api;

import java.util.ArrayList;
import java.util.List;

public class RequestAll<T> extends Request<List<T>> {
    private RequestGetList<T> initialRequest;

    public RequestAll(RequestGetList<T> initialRequest) {
        super(initialRequest.syncano);
        this.initialRequest = initialRequest;
    }

    @Override
    public ResponseGetList<T> send() {
        ResponseGetList<T> r = initialRequest.send();
        if (!r.isSuccess()) {
            return r;
        }
        ArrayList<T> data = new ArrayList<>(r.getData());
        while (r.hasNextPage()) {
            r = r.getNextPage();
            if (!r.isSuccess()) {
                return r;
            } else {
                data.addAll(r.getData());
            }
        }

        ResponseGetList<T> response = new ResponseGetList<>(syncano, initialRequest.resultType);
        response.setData(data);
        response.setResultCode(Response.CODE_SUCCESS);
        response.setHttpResultCode(Response.HTTP_CODE_SUCCESS);
        return response;
    }
}
