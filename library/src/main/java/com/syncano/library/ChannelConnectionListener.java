package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;

public interface ChannelConnectionListener {
    void onNotification(Notification notification);

    void onError(Response<Notification> response);
}
