package com.syncano.android.lib.api;

import com.google.gson.Gson;
import com.syncano.android.lib.Constants;
import com.syncano.android.lib.Syncano;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;

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

    protected LinkedHashMap<String, String> prepareParams()
    {
        // Find params using reflection and annotation to fields.
        return null;
    }
}
