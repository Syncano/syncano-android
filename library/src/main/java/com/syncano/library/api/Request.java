package com.syncano.library.api;

import com.google.gson.Gson;
import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.utils.GsonHelper;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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
    public List<NameValuePair> prepareUrlParams() {
        return null;
    }

    /**
     * Get url params for requests.
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getUrlParams() {
        List<NameValuePair> params = prepareUrlParams();

        if (params == null) {
            return null;
        }

        StringBuilder postData = new StringBuilder();
        for (NameValuePair param : params) {

            if (postData.length() != 0) postData.append('&');
            else postData.append("?");

            String name = null;
            String value = null;

            try {
                name = URLEncoder.encode(param.getName(), "UTF-8");
                value = URLEncoder.encode(param.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            postData.append(name).append('=').append(value);
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
        syncano.requestAsync(this, callback);
    }
}
