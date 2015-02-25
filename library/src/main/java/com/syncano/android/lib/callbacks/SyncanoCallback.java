package com.syncano.android.lib.callbacks;

import com.syncano.android.lib.api.SyncanoException;

public interface SyncanoCallback {

    public void failure(SyncanoException error);
}
