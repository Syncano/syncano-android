package com.syncano.library.data;

import com.syncano.library.utils.SyncanoLog;

public class Webhook extends CustomWebhook<Trace> {

    public Webhook(String name) {
        super(Trace.class, name);
    }

    public Webhook(String name, int codebox) {
        super(Trace.class, name, codebox);
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
