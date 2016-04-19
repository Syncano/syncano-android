package com.syncano.library.offline;

import com.syncano.library.Syncano;
import com.syncano.library.api.Request;
import com.syncano.library.callbacks.SyncanoCallback;

public abstract class OfflineRequest<T> extends Request<T> {
    private SyncanoCallback<T> backgroundCallback = null;

    public OfflineRequest(Syncano syncano) {
        super(syncano);
    }

    public OfflineRequest<T> setBackgroundCallback(SyncanoCallback<T> callback) {
        backgroundCallback = callback;
        return this;
    }

    public SyncanoCallback<T> getBackgroundCallback() {
        return backgroundCallback;
    }

    public void doInBackground(Request<T> request) {
        if (backgroundCallback == null) {
            return;
        }
        request.sendAsync(backgroundCallback);
    }
}
