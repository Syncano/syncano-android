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
        Response<T> response = null;
        switch (mode) {
            case ONLINE:
                response = doOnlineRequest(onlineRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
                afterRequests(onlineRequest);
                break;
            case LOCAL_WHEN_ONLINE_FAILED:
                Response<T> onlineResponse = doOnlineRequest(onlineRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
                if (onlineResponse.isSuccess()) {
                    response = onlineResponse;
                } else {
                    response = doLocalRequest(onlineRequest);
                }
                afterRequests(onlineRequest);
                break;
            case LOCAL_ONLINE_IN_BACKGROUND:
                response = doLocalRequest(onlineRequest);
                Request.runAsync(new Request<T>(null) {
                    @Override
                    public Response<T> send() {
                        Response<T> response = doOnlineRequest(onlineRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
                        afterRequests(onlineRequest);
                        return response;
                    }
                }, backgroundCallback);
                break;
            case LOCAL:
                response = doLocalRequest(onlineRequest);
                afterRequests(onlineRequest);
                break;
        }

        return response;
    }

    public void afterRequests(ResultRequest<T> onlineRequest) {
    }

    public abstract Response<T> doOnlineRequest(ResultRequest<T> onlineRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage);

    public abstract Response<T> doLocalRequest(ResultRequest<T> onlineRequest);
}
