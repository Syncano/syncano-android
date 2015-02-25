package com.syncano.android.lib.callbacks;

public interface GetOneCallback <T> extends SyncanoCallback {
    public void success (T object);
}
