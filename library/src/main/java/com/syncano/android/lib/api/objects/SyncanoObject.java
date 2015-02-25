package com.syncano.android.lib.api.objects;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.syncano.android.lib.api.Params;
import com.syncano.android.lib.api.SyncanoBase;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.callbacks.GetOneCallback;
import com.syncano.android.lib.data.DataObject;
import com.syncano.android.lib.data.Page;
import com.syncano.android.lib.data.PageInternal;

import java.util.ArrayList;

public class SyncanoObject extends SyncanoBase {

    private static final String LIST_URL = "/v1/instances/%s/classes/%s/objects/";
    private static final String DETAIL_URL = "/v1/instances/%s/classes/%s/objects/%d/";

    public <T extends DataObject> void create(T object, GetOneCallback<T> callback)
    {
        String className = getSyncanoClassName(object.getClass());
        String url = String.format(LIST_URL, getSyncanoInstance(), className);
        requestDetail(METHOD_POST, url, null, callback);
    }

    public <T extends DataObject> void getOne(int id, GetOneCallback<T> callback)
    {
    }

    public static <T extends DataObject> void get(Params params, GetCallback<T> callback)
    {

    }

    public static <T extends DataObject> void getNextPage(Params params, int id, GetCallback<T> callback)
    {
    }

    public static <T extends DataObject> void getPrevPage(Params params, int id, GetCallback<T> callback)
    {
    }

    public static <T extends DataObject> void update(T object, GetOneCallback<T> callback)
    {
    }

    public static <T extends DataObject> void delete(int id, GetOneCallback<T> callback)
    {
    }




    /*public static  <C> Response<Page<C>> get(String instanceName, Class<C> type)
    {
        String json = "{\"next\": \"/v1/instances/syncano/codeboxes/1/schedules/4/traces/?direction=1&last_pk=11268\",\"prev\": \"/v1/instances/syncano/codeboxes/1/schedules/4/traces/?direction=0&last_pk=11367\",\"objects\": [{\"id\" : 666}, {\"id\" : 555}]}";
        PageInternal resultPage = new Gson().fromJson(json, PageInternal.class);

        ArrayList<C> list = new ArrayList<>();
        Page<C> page = new Page<>();

        for (JsonElement jsonElement : resultPage.getObjects())
        {
            C item = new Gson().fromJson(jsonElement, type);
            list.add(item);
        }

        page.setObjects(list);
        Response<Page<C>> response = new Response<>();
        response.setData(page);

        //return send(type, METHOD_GET, URL, instanceName, getSyncanoClassName(resultType));
        return response;
    }*/
}
