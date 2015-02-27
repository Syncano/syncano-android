package com.syncano.android.lib.callbacks;

public interface GetCallback <T> extends SyncanoCallback {
    public void success (T result);
}
