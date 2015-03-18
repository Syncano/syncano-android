package com.syncano.android.lib.api;

import com.google.gson.Gson;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.callbacks.SyncanoCallback;
import com.syncano.android.lib.utils.GsonHelper;

import java.io.UnsupportedEncodingException;
import java.util.Map;

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
    public String prepareParams() {
        return null;
    }

    /**
     * Prepare URL params.
     * @return
     */
    protected Map<String, String> prepareUrlParams() {
        return null;
    }

    /**
     * Get url params for requests.
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getUrlParams() {
        Map<String, String> params = prepareUrlParams();

        if (params == null) {
            return null;
        }

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {

            if (postData.length() != 0) postData.append('&');
            else postData.append("?");

            postData.append(param.getKey());
            postData.append('=');
            postData.append(String.valueOf(param.getValue()));

        }

        return postData.toString();
    }

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
