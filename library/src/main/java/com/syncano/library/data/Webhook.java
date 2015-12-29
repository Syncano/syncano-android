package com.syncano.library.data;

import com.syncano.library.utils.SyncanoLog;

public class Webhook extends CustomWebhook<Trace> {
    public Webhook(String name, int codebox) {
        super(name, codebox);
    }

    public Webhook(String name) {
        super(name);
    }

    public String getOutput() {
        if (getResult() != null) {
            return getResult().getOutput();
        }
        SyncanoLog.d(CustomWebhook.class.getSimpleName(), "Getting output, without calling run() first");
        return null;
    }

    public String getErrorOutput() {
        if (getResult() != null) {
            return getResult().getErrorOutput();
        }
        SyncanoLog.d(CustomWebhook.class.getSimpleName(), "Getting output, without calling run() first");
        return null;
    }

}
