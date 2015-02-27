package com.syncano.android.lib;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.PageInternal;
import com.syncano.android.lib.api.SyncanoException;
import com.syncano.android.lib.callbacks.DeleteCallback;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.utils.GsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public abstract class SyncanoBase {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";

    private String apiKey;
    private String instance;
    private HttpClient httpClient = null;

    private Gson gson;

    protected SyncanoBase(String apiKey, String instance, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.instance = instance;
        this.httpClient = httpClient;
        gson = GsonHelper.createGson();
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    protected static String getSyncanoClassName(Class<?> clazz) {
        SyncanoClass syncanoClass = clazz.getAnnotation(SyncanoClass.class);

        if (syncanoClass == null) {
            throw new RuntimeException("Class " + clazz.getSimpleName() + " is not marked with SyncanoClass annotation.");
        }

        return syncanoClass.name();
    }

    protected <T> void requestCreate(Class<T> type, String url, Object params, GetCallback<T> callback) {
        try {
            String json = request(METHOD_POST, url, params);
            T result = gson.fromJson(json, type);

            callback.success(result);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected <T> void requestGetOne(Class<T> type, String url, GetCallback<T> callback) {
        requestGetOne(type, url, null, callback);
    }

    protected <T> void requestGetOne(Class<T> type, String url, Object params, GetCallback<T> callback) {
        try {
            String json = request(METHOD_GET, url, params);
            T result = gson.fromJson(json, type);

            callback.success(result);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected <T> void requestGetList(Class<T> type, String url, GetCallback<List<T>> callback) {
        requestGetList(type, url, null, callback);
    }

    protected <T> void requestGetList(Class<T> type, String url, Object params, GetCallback<List<T>> callback) {
        try {
            String json = request(METHOD_GET, url, params);

            Type listType = new TypeToken<ArrayList<T>>(){}.getType();
            ArrayList<T> result = gson.fromJson(json, listType);

            callback.success(result);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected <T> void requestGetPage(Class<T> type, String url, GetCallback<T> callback) {
        requestGetPage(type, url, callback);
    }

    protected <T> void requestGetPage(Class<T> type, String url, Object params, GetCallback<Page<T>> callback) {
        try {
            String json = request(METHOD_GET, url, params);
            PageInternal pageInternal = gson.fromJson(json, PageInternal.class);

            Page<T> resultPage = new Page<>();
            resultPage.setNext(pageInternal.getNext());
            resultPage.setPrev(pageInternal.getPrev());

            List<T> resultList = new ArrayList<>();

            if (pageInternal.getObjects() != null) {

                for (JsonElement element : pageInternal.getObjects()) {
                    resultList.add(gson.fromJson(element, type));
                }
            }
            resultPage.setObjects(resultList);

            callback.success(resultPage);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected <T> void requestUpdate(Class<T> type, String url, Object params, GetCallback<T> callback) {
        try {
            String json = request(METHOD_PATCH, url, params);
            T result = gson.fromJson(json, type);

            callback.success(result);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected void requestDelete(String url, DeleteCallback callback) {
        try {
            request(METHOD_DELETE, url, null);
            callback.success();
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    /**
     * Send request and receive json.
     * @param requestMethod
     * @param url
     * @param params
     * @return
     * @throws SyncanoException
     */
    protected String request(String requestMethod, String url, Object params) throws SyncanoException {
        if (params != null) {
            String jsonParams = gson.toJson(params);
            return httpClient.sendRequest(getApiKey(), requestMethod, Constants.SERVER_URL + url, jsonParams);
        } else {
            return httpClient.sendRequest(getApiKey(), requestMethod, Constants.SERVER_URL + url);
        }
    }
}
