package com.syncano.android.lib;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.PageInternal;
import com.syncano.android.lib.api.Params;
import com.syncano.android.lib.api.SyncanoException;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.callbacks.GetOneCallback;
import com.syncano.android.lib.utils.GsonHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    protected SyncanoBase(String apiKey, String instance, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.instance = instance;
        this.httpClient = httpClient;
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

    protected <T> void requestList(Class<T> type, String requestMethod, String url, Params params, GetCallback<T> callback)
    {
        try {
            String json = httpClient.sendRequest(getApiKey(), requestMethod, Constants.SERVER_URL + url);
            Gson gson = GsonHelper.createGson();
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

    protected <T> void requestDetail(Class<T> type, String requestMethod, String url, GetOneCallback<T> callback)
    {
        try {
            String json = httpClient.sendRequest(getApiKey(), requestMethod, Constants.SERVER_URL + url);
            T result = GsonHelper.createGson().fromJson(json, type);

            callback.success(result);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected static LinkedHashMap<String, String> prepareParams(Object o)
    {
        Field[] allFields = o.getClass().getDeclaredFields();

        for (Field field : allFields)
        {
            field.setAccessible(true);

            try {
                String fieldName;
                SyncanoParam syncanoParam = field.getAnnotation(SyncanoParam.class);

                if (syncanoParam != null && syncanoParam.name() != null && syncanoParam.name().isEmpty() == false) {
                    fieldName = syncanoParam.name();
                }
                else {
                    fieldName = field.getName();
                }
                Log.d("test", "name = " + fieldName + " value= " + field.get(o));
            }
            catch (IllegalAccessException e)
            {
                Log.d("BaseApiMethod", e.toString());
            }

        }
        return null;
    }
}
