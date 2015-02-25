package com.syncano.android.lib.callbacks;

import com.syncano.android.lib.api.Page;

import java.util.List;

public interface GetCallback <T> extends SyncanoCallback {
    public void success (Page<T> page);
}
