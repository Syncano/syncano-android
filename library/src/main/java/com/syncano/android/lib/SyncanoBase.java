package com.syncano.android.lib;


import com.google.gson.Gson;
import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.api.Request;
import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.api.SyncanoException;
import com.syncano.android.lib.utils.GsonHelper;
import com.syncano.android.lib.utils.SyncanoHttpClient;

public abstract class SyncanoBase {

    private static final String TAG = SyncanoBase.class.getSimpleName();

    private String apiKey;
    private String instance;

    private Gson gson;

    protected SyncanoBase(String apiKey, String instance) {
        this.apiKey = apiKey;
        this.instance = instance;
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

    /*protected <T> void requestPost(Class<T> type, String url, Object params, GetCallback<T> callback) {
        try {
            String json = request(SimpleHttpClient.METHOD_POST, url, params);
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
            String json = request(SimpleHttpClient.METHOD_GET, url, params);
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
            String json = request(SimpleHttpClient.METHOD_GET, url, params);

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
            String json = request(SimpleHttpClient.METHOD_GET, url, params);
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
            String json = request(SimpleHttpClient.METHOD_PATCH, url, params);
            T result = gson.fromJson(json, type);

            callback.success(result);
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }

    protected void requestDelete(String url, DeleteCallback callback) {
        try {
            request(SimpleHttpClient.METHOD_DELETE, url, null);
            callback.success();
        } catch (SyncanoException e) {
            callback.failure(e);
        }
    }*/

    /**
     * Send request and receive json.
     * @param syncanoRequest
     * @return
     * @throws SyncanoException
     */
    public Response request(Request<?> syncanoRequest) {

        SyncanoHttpClient http = new SyncanoHttpClient();
        http.setTimeout(0);

        return http.send(syncanoRequest, apiKey);
    }
}
