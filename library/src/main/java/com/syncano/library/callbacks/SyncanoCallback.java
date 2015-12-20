package com.syncano.library.callbacks;

import com.syncano.library.api.Response;

public interface SyncanoCallback<T> {

    void success(Response<T> response, T result);

    void failure(Response<T> response);
}
