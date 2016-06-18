package com.syncano.library.api;

import com.syncano.library.PlatformType;
import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Request<T> {

    private static final ExecutorService requestExecutor = Executors.newFixedThreadPool(3);
    protected Syncano syncano;

    public Request(Syncano syncano) {
        this.syncano = syncano;
    }

    public interface RunAfter<T> {
        void run(Response<T> response);
    }

    public Response<T> instantiateResponse() {
        return new Response<>();
    }

    public abstract Response<T> send();

    public Syncano getSyncano() {
        return syncano;
    }

    /**
     * Send asynchronous request. There asynchronous requests may
     * be executed same time (three Threads). If there are more requests, they
     * are waiting in queue.
     *
     * @param callback Callback to notify when request receives response.
     */
    public void sendAsync(final SyncanoCallback<T> callback) {
        runAsync(this, callback);
    }

    public static <T> void runAsync(final Request<T> request, final SyncanoCallback<T> callback) {
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Response<T> response = request.send();
                if (callback == null) {
                    return;
                }
                PlatformType.get().runOnCallbackThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccess()) {
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
