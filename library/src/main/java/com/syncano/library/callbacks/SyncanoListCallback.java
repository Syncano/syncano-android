package com.syncano.library.callbacks;

import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;

import java.util.List;

public abstract class SyncanoListCallback<T> implements SyncanoCallback<List<T>> {
    @Override
    public void success(Response<List<T>> response, List<T> result) {
        success((ResponseGetList<T>) response, result);
    }

    @Override
    public void failure(Response<List<T>> response) {
        failure((ResponseGetList<T>) response);
    }

    public abstract void success(ResponseGetList<T> response, List<T> result);

    public abstract void failure(ResponseGetList<T> response);
}
