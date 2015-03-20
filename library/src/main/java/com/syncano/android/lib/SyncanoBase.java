package com.syncano.android.lib;


import android.util.Log;

import com.google.gson.Gson;
import com.syncano.android.lib.annotation.SyncanoClass;
import com.syncano.android.lib.api.Request;
import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.callbacks.SyncanoCallback;
import com.syncano.android.lib.utils.GsonHelper;
import com.syncano.android.lib.utils.SyncanoHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class SyncanoBase {

    private static final String TAG = SyncanoBase.class.getSimpleName();

    private String apiKey;
    private String instance;

    private Gson gson;
    private ExecutorService requestExecutor = Executors.newFixedThreadPool(3);

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

    /**
     * Send request and receive json.
     * @param syncanoRequest
     * @return
     */
    public Response request(Request<?> syncanoRequest) {

        SyncanoHttpClient http = new SyncanoHttpClient();
        http.setTimeout(0);

        return http.send(syncanoRequest, apiKey);
    }

    public <T> void requestAsync(final Request<T> syncanoRequest, final SyncanoCallback<T> callback) {
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Response<T> response = syncanoRequest.send();
                if (response.isOk()) {
                    callback.success(response, response.getData());
                } else {
                    callback.failure(response);
                }
            }
        });
    }
}
