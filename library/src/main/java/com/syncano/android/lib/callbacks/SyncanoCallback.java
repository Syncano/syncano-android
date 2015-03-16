package com.syncano.android.lib.callbacks;

import com.syncano.android.lib.api.Response;

public interface SyncanoCallback <T> {

    public void success (Response response, T result);
    public void failure(Response response);
}
