package com.syncano.library.callbacks;

import com.syncano.library.api.Response;

public interface SyncanoCallback<T> {

    public void success (Response<T> response, T result);
    public void failure(Response<T> response);
}
