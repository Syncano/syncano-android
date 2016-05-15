package com.syncano.library.offline;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Request;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoObject;

/**
 * Wrapper for a request, that adds offline storage function
 *
 * @param <T> Type of objects to get
 */
public abstract class OfflineRequest<T> extends Request<T> {

    private ResultRequest<T> onlineRequest;
    private OfflineMode mode;
    private Boolean cleanStorageOnSuccessDownload;
    private Boolean saveDownloadedDataToStorage;
    private SyncanoCallback<T> backgroundCallback = null;

    public OfflineRequest(ResultRequest<T> onlineRequest) {
        super(onlineRequest.getSyncano());
        if (!SyncanoObject.class.isAssignableFrom(onlineRequest.getResultType())) {
            throw new RuntimeException("Using offline storage is only possible for SyncanoObject objects");
        }
        this.onlineRequest = onlineRequest;
        SyncanoClass classAnnotation = ((SyncanoClass) onlineRequest.getResultType().getAnnotation(SyncanoClass.class));
        this.mode = classAnnotation.getMode();
        this.cleanStorageOnSuccessDownload = classAnnotation.cleanStorageOnSuccessDownload();
        this.saveDownloadedDataToStorage = classAnnotation.saveDownloadedDataToStorage();
    }

    public OfflineRequest<T> setBackgroundCallback(SyncanoCallback<T> callback) {
        backgroundCallback = callback;
        return this;
    }

    public SyncanoCallback<T> getBackgroundCallback() {
        return backgroundCallback;
    }

    public void doInBackground(Request<T> request) {
        if (backgroundCallback == null) {
            return;
        }
        request.sendAsync(backgroundCallback);
    }

    public OfflineRequest<T> mode(OfflineMode mode) {
        this.mode = mode;
        return this;
    }

    public OfflineRequest<T> cleanStorageOnSuccessDownload(boolean clean) {
        this.cleanStorageOnSuccessDownload = clean;
        return this;
    }

    public OfflineRequest<T> saveDownloadedDataToStorage(boolean save) {
        this.saveDownloadedDataToStorage = save;
        return this;
    }

    @Override
    public Response<T> send() {
        switch (mode) {
            case ONLINE:
                return doOnlineRequest(onlineRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
            case LOCAL_WHEN_ONLINE_FAILED:
                Response<T> onlineResponse = doOnlineRequest(onlineRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
                if (onlineResponse.isSuccess()) {
                    return onlineResponse;
                } else {
                    return doLocalRequest(onlineRequest);
                }
            case LOCAL_ONLINE_IN_BACKGROUND:
                doInBackground(onlineRequest);
                return doLocalRequest(onlineRequest);
            case LOCAL:
                return doLocalRequest(onlineRequest);
        }

        return null;
    }

    public abstract Response<T> doOnlineRequest(ResultRequest<T> onlineRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage);

    public abstract Response<T> doLocalRequest(ResultRequest<T> onlineRequest);
}
