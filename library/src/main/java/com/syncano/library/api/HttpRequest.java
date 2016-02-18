package com.syncano.library.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.syncano.library.Syncano;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.utils.SyncanoHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class HttpRequest<T> extends Request<T> {

    protected Gson gson;
    private List<NameValuePair> urlParams = new ArrayList<>();
    private List<NameValuePair> httpHeaders = new ArrayList<>();
    private String url;
    private String path;
    private String completeCustomUrl;
    private HashSet<Integer> correctHttpResponse = new HashSet<>();
    private boolean longConnectionTimeout = false;

    protected HttpRequest(String path, Syncano syncano) {
        super(syncano);
        this.path = path;
        this.url = syncano.getUrl();
        gson = GsonParser.createGson();
        if (syncano.getApiKey() != null && !syncano.getApiKey().isEmpty()) {
            setHttpHeader("X-API-KEY", syncano.getApiKey());
        }
        if (syncano.getUserKey() != null && !syncano.getUserKey().isEmpty()) {
            setHttpHeader("X-USER-KEY", syncano.getUserKey());
        }
    }

    /**
     * Http request method: POST, GET, PUT, PATCH, UPDATE, DELETE
     */
    public abstract String getRequestMethod();

    /**
     * Prepare parameters for request.
     */
    public HttpEntity prepareParams() {
        return null;
    }

    /**
     * Prepare json parameters for request.
     */
    public JsonElement prepareJsonParams() {
        return null;
    }

    /**
     * Prepare URL params.
     */
    public void prepareUrlParams() {
    }

    public void addCorrectHttpResponseCode(int code) {
        correctHttpResponse.add(code);
    }

    public boolean isCorrectHttpResponseCode(int code) {
        return correctHttpResponse.contains(code);
    }

    /**
     * Add custom params to url.
     * Used most for get queries.
     *
     * @param key   Parameter key.
     * @param value Parameter value.
     */
    public void addUrlParam(String key, String value) {
        urlParams.add(new BasicNameValuePair(key, value));
    }

    /**
     * Get url params for requests.
     */
    public String getUrlParams() {

        prepareUrlParams();
        if (urlParams == null || urlParams.isEmpty()) {
            return "";
        }

        StringBuilder postData = new StringBuilder();
        for (NameValuePair param : urlParams) {

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

    /**
     * Add additional header like social authentication token.
     *
     * @param name  Header name.
     * @param value Header value.
     */
    public void setHttpHeader(String name, String value) {
        httpHeaders.add(new BasicNameValuePair(name, value));
    }

    /**
     * Get additional headers for request.
     *
     * @return Headers.
     */
    public List<NameValuePair> getHttpHeaders() {
        return httpHeaders;
    }

    public String getUrlPath() {
        return path;
    }

    public String getUrl() {
        String fullUrl;
        if (getCompleteCustomUrl() != null) {
            fullUrl = getCompleteCustomUrl() + getUrlParams();
        } else {
            fullUrl = url + getUrlPath() + getUrlParams();
        }
        return fullUrl;
    }

    public abstract T parseResult(Response<T> response, String json);

    public Response<T> send() {
        SyncanoHttpClient http = new SyncanoHttpClient();
        if (getLongConnectionTimeout()) {
            // 5 minutes is required for channel connection
            // so setting 6
            http.setTimeout(6 * 60 * 1000);
        }
        Response<T> response = http.send(this);

        if (getRunAfter() != null) {
            getRunAfter().run(response);
        }
        return response;
    }

    public String getCompleteCustomUrl() {
        return completeCustomUrl;
    }

    public void setCompleteCustomUrl(String completeCustomUrl) {
        this.completeCustomUrl = completeCustomUrl;
    }

    public String getContentType() {
        return "application/json";
    }

    public void setLongConnectionTimeout() {
        longConnectionTimeout = true;
    }

    public boolean getLongConnectionTimeout() {
        return longConnectionTimeout;
    }
}
