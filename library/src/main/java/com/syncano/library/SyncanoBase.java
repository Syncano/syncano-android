package com.syncano.library;


import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.syncano.library.api.Request;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.utils.GsonHelper;
import com.syncano.library.utils.SyncanoHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class SyncanoBase {

    protected String customServerUrl;
    protected String apiKey;
    protected String userKey;
    protected String instanceName;

    protected Gson gson;
    protected ExecutorService requestExecutor = Executors.newFixedThreadPool(3);

    protected SyncanoBase(String apiKey, String instanceName) {
        this.apiKey = apiKey;
        this.instanceName = instanceName;
        gson = GsonHelper.createGson();
    }

    protected SyncanoBase(String customServerUrl, String apiKey, String instanceName) {
        this(apiKey, instanceName);
        this.customServerUrl = customServerUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * User authentication key used for all requests.
     *
     * @return User authentication key.
     */
    public String getUserKey() {
        return userKey;
    }

    /**
     * Setting user key here will cause adding it automatically to every request just before it's sent. To get
     * authorization key, use loginUser method on Syncano object.
     *
     * @param userKey After loginUser, it's available under user.getUserKey().
     */
    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instance) {
        this.instanceName = instance;
    }

    /**
     * Send synchronous http request.
     *
     * @param syncanoRequest Request with url, method and parameters.
     * @return Response for request.
     */
    public <T> Response<T> request(Request<T> syncanoRequest) {

        SyncanoHttpClient http = new SyncanoHttpClient();
        http.setTimeout(0);

        if (apiKey != null && !apiKey.isEmpty()) {
            syncanoRequest.setHttpHeader("X-API-KEY", apiKey);
        }

        if (userKey != null && !userKey.isEmpty()) {
            syncanoRequest.setHttpHeader("X-USER-KEY", userKey);
        }

        Response<T> response;
        if (customServerUrl != null && !customServerUrl.isEmpty()) {
            response = http.send(customServerUrl, syncanoRequest);
        } else {
            response = http.send(Constants.PRODUCTION_SERVER_URL, syncanoRequest);
        }
        if (syncanoRequest.getRunAfter() != null) {
            syncanoRequest.getRunAfter().run(response);
        }
        return response;
    }

    /**
     * Send asynchronous http request. There asynchronous requests may
     * be executed same time (three Threads). If there is more requests, they
     * are waiting in queue.
     *
     * @param syncanoRequest Request with url, method and parameters.
     * @param callback       Callback to notify when request receives response.
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
