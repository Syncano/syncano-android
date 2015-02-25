package com.syncano.android.lib;

import android.util.Log;

import com.syncano.android.lib.api.Params;
import com.syncano.android.lib.callbacks.GetCallback;
import com.syncano.android.lib.callbacks.GetOneCallback;
import com.syncano.android.lib.data.SyncanoObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class Syncano extends SyncanoBase {

    private static Syncano sharedInstance = null;




    /**
     * Create Syncano object.
     * @param apiKey Api key.
     * @param instance Syncano instance related with apiKey.
     */
    public Syncano(String apiKey, String instance) {
        super(apiKey, instance, new HttpClient());
    }

    /**
     * Create static Syncano instance.
     * @param apiKey Api key.
     * @param instance Syncano instance related with apiKey.
     */
    public static void initSharedInstance(String apiKey, String instance) {
        sharedInstance = new Syncano(apiKey, instance);
    }

    public static Syncano getSharedInstance() {
        return sharedInstance;
    }

    // ==================== Objects ==================== //

    public <T extends SyncanoObject> void createObject(T object, GetOneCallback<T> callback)
    {
        Class<T> type = (Class<T>) object.getClass();
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        requestDetail(type, METHOD_POST, url, callback);
    }

    public <T extends SyncanoObject> void getObject(int id, GetOneCallback<T> callback)
    {

    }

    public <T extends SyncanoObject> void getObjects(Class<T> type, Params params, GetCallback<T> callback)
    {
        String className = getSyncanoClassName(type);
        String url = String.format(Constants.OBJECTS_LIST_URL, getInstance(), className);
        requestList(type, METHOD_GET, url, null, callback);
    }

    public <T extends SyncanoObject> void getObjectsNextPage(int id, Params params, GetCallback<T> callback)
    {
    }

    public <T extends SyncanoObject> void getObjectsPrevPage(int id, Params params, GetCallback<T> callback)
    {
    }

    public static <T extends SyncanoObject> void updateObjects(T object, GetOneCallback<T> callback)
    {
    }

    public static <T extends SyncanoObject> void deleteObjects(int id, GetOneCallback<T> callback)
    {
    }
}