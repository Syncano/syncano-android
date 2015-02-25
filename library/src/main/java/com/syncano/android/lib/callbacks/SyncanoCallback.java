package com.syncano.android.lib.callbacks;

import com.syncano.android.lib.api.SyncanoError;

public interface SyncanoCallback {

    public void failure(SyncanoError error);
}
