package com.syncano.library.android;

import android.os.Handler;
import android.os.Looper;

import com.syncano.library.Syncano;
import com.syncano.library.api.Request;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;

public class SyncanoAndroid extends Syncano {
    /**
     * Create Syncano object.
     *
     * @param apiKey   Api key.
     * @param instance Syncano instance related with apiKey.
     */
    public SyncanoAndroid(String apiKey, String instance) {
        super(apiKey, instance);
    }

    @Override
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
