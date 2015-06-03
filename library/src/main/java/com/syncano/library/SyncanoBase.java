package com.syncano.library;


import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Request;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.utils.GsonHelper;
import com.syncano.library.utils.SyncanoHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class SyncanoBase {

    private static final String TAG = SyncanoBase.class.getSimpleName();

    protected String apiKey;
    protected String userKey;
    protected String instance;

    protected Gson gson;
    protected ExecutorService requestExecutor = Executors.newFixedThreadPool(3);

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

    /**
     * User authentication key used for all requests.
     * @return User authentication key.
     */
    public String getUserKey() {
        return userKey;
    }

    /**
     * Setting user key here will cause adding it automatically to every request just before it's sent. To get
     * authorization key, use authUser method on Syncano object.
     * @param userKey After authUser, it's available under user.getUserKey().
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    /**
     * DataObjects endpoints are using class names in path.
     * Class must be marked with SyncanoClass annotation.
     *
     * @param clazz Class to extract class name.
     * @return Class name from SyncanoClass annotation.
     */
    public static String getSyncanoClassName(Class<?> clazz) {
        SyncanoClass syncanoClass = clazz.getAnnotation(SyncanoClass.class);

        if (syncanoClass == null) {
            throw new RuntimeException("Class " + clazz.getSimpleName() + " is not marked with SyncanoClass annotation.");
        }

        return syncanoClass.name();
    }

    /**
     * Send synchronous http request.
     * @param syncanoRequest Request with url, method and parameters.
     * @return Response for request.
     */
    public Response request(Request<?> syncanoRequest) {

        SyncanoHttpClient http = new SyncanoHttpClient();
        http.setTimeout(0);

        syncanoRequest.setHttpHeader("X-API-KEY", apiKey);

        if (userKey != null && !userKey.isEmpty()) {
            syncanoRequest.setHttpHeader("X-USER-KEY", userKey);
        }

        return http.send(syncanoRequest);
    }

    /**
     * Send asynchronous http request. There asynchronous requests may
     * be executed same time (three Threads). If there is more requests, they
     * are waiting in queue.
     * @param syncanoRequest Request with url, method and parameters.
     * @param callback Callback to notify when request receives response.
     */
    public <T> void requestAsync(final Request<T> syncanoRequest, final SyncanoCallback<T> callback) {
        final Handler handler = new Handler(Looper.getMainLooper());

        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Response<T> response = syncanoRequest.send();

                // Post result to UI thread.
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isOk()) {
                            callback.success(response, response.getData());
                        } else {
                            callback.failure(response);
                        }
                    }
                });
            }
        });
    }
}
