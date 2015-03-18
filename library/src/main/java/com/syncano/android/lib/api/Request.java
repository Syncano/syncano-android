package com.syncano.android.lib.api;

import com.google.gson.Gson;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.callbacks.SyncanoCallback;
import com.syncano.android.lib.utils.GsonHelper;

import java.lang.reflect.Type;

public abstract class Request<T> {

    protected Gson gson;
    private String url;
    private Syncano syncano;

    protected Request(String url, Syncano syncano) {
        this.url = url;
        this.syncano = syncano;
        gson = GsonHelper.createGson();
    }

    /**
     * Http request method: POST, GET, PUT, PATCH, UPDATE, DELETE
     * @return
     */
    public abstract String getRequestMethod();

    /**
     * Prepare json parameters for request.
     * @return
     */
    public abstract String prepareParams();

    public String getUrl() {
        return url;
    }

    public abstract T parseResult(String json);

    public Response<T> send() {
        return syncano.request(this);
    }

    public void sendAsync(SyncanoCallback<T> callback) {
        // call run() on new Thread
    }
}
