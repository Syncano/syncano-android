package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;

public interface SyncServerListener {
    void onMessage(Notification notification);
    void onError(Response<Notification> response);
}
