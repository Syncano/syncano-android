package com.syncano.library.offline;

import com.syncano.library.Syncano;
import com.syncano.library.api.Request;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;

import java.util.List;

// TODO implement similar way as RequestAll
public class OfflineRequest<T> extends Request<List<T>> {
    SyncanoCallback<List<T>> backgroundCallback;

    public OfflineRequest(Syncano syncano) {
        super(syncano);
    }

    public OfflineRequest<T> setBackgroundCallback(SyncanoCallback<List<T>> callback) {
        backgroundCallback = callback;
        return this;
    }

    @Override
    public Response<List<T>> send() {
        // TODO
        return null;
    }
}
