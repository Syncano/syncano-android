package com.syncano.android.lib.api;

import com.google.gson.Gson;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.callbacks.SyncanoCallback;
import com.syncano.android.lib.utils.GsonHelper;

public abstract class Request <T> {

    private Class<T> responseType;
    private String url;
    protected Gson gson;
    private Syncano syncano;


    protected Request(Class<T> responseType, String url, Syncano syncano) {
        this.responseType = responseType;
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

    public T parseResult(String json) {
        return gson.fromJson(json, responseType);
    }

    public Response<T> send() {

        return syncano.request(this);
    }

    public void sendAsync(SyncanoCallback<T> callback) {
        // call run() on new Thread
    }
}
