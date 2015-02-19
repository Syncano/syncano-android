package com.syncano.android.lib.api;

import android.util.Log;

import com.google.gson.Gson;
import com.syncano.android.lib.Constants;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.data.Invitation;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public abstract class BaseApiMethod <T> {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_PATCH = "PATCH";
    public static final String METHOD_DELETE = "DELETE";

    /**
     * @return GET / POST / PUT / PATCH / DELETE
     */
    protected abstract String getRequestMethod();

    /**
     * @return Syncano API method to call
     */
    protected abstract String getMethod();

    protected abstract Type getResultType();

    public Response<T> send(Syncano syncano)
    {
        Response<T> response = new Response<T>();
        String json = syncano.getHttpClient().sendRequest(getRequestMethod(), Constants.SERVER_URL + getMethod(), response);
        response.setData(parseResult(json));
        return response;
    }

    protected T parseResult(String json)
    {
        return new Gson().fromJson(json, getResultType());
    }

    protected LinkedHashMap<String, String> prepareParams(Object o)
    {
        Field[] allFields = o.getClass().getDeclaredFields();

        for (Field field : allFields)
        {
            field.setAccessible(true);

            try {
                String fieldName;
                SyncanoParam syncanoParam = field.getAnnotation(SyncanoParam.class);

                if (syncanoParam != null)
                {
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
