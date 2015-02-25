package com.syncano.android.lib.api;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.Constants;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.callbacks.GetOneCallback;
import com.syncano.android.lib.data.AdminInvitation;
import com.syncano.android.lib.data.Page;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;

public abstract class SyncanoBase {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";

    protected static String getSyncanoClassName(Class<?> clazz) {
        SyncanoClass syncanoClass = clazz.getAnnotation(SyncanoClass.class);

        if (syncanoClass == null) {
            throw new RuntimeException("Class " + clazz.getSimpleName() + " is not marked with SyncanoClass annotation.");
        }

        return syncanoClass.name();
    }

    protected String getSyncanoInstance()
    {
        return "testInstance";
    }

    protected static <T> void requestList(String requestMethod, String url, Params params, GetCallback<T> callback)
    {

    }

    protected static <T> void requestDetail(String requestMethod, String url, Params params, GetOneCallback<T> callback)
    {

    }



















    //----------------------------------------//
    /*public static <T> Response<T> send(Class<T> resultType, String requestMethod, String url, Object... urlParams)
    {
        Response<T> response = new Response<T>();
        String json = Syncano.getInstance().getHttpClient().sendRequest(requestMethod, String.format(url, urlParams), response);
        response.setData(parseResult(json, resultType));
        return response;
    }*/

    protected static <T> T parseResult(String json, Class<T> resultType)
    {
        return new Gson().fromJson(json, resultType);
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
