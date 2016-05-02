package com.syncano.library.offline;

import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.data.SyncanoObject;

/**
 * Wrapper for a request, that adds offline storage function
 *
 * @param <T> Type of objects to get
 */
public abstract class OfflineGetRequest<T> extends OfflineRequest<T> {

    private ResultRequest<T> getRequest;
    private OfflineMode mode = OfflineMode.ONLINE;
    private boolean cleanStorageOnSuccessDownload = false;
    private boolean saveDownloadedDataToStorage = false;

    public OfflineGetRequest(ResultRequest<T> getRequest) {
        super(getRequest.getSyncano());
        if (!SyncanoObject.class.isAssignableFrom(getRequest.getResultType())) {
            throw new RuntimeException("Using offline storage is only possible for SyncanoObject objects");
        }
        this.getRequest = getRequest;
    }

    public OfflineGetRequest<T> mode(OfflineMode mode) {
        this.mode = mode;
        return this;
    }

    public OfflineGetRequest<T> cleanStorageOnSuccessDownload(boolean clean) {
        this.cleanStorageOnSuccessDownload = clean;
        return this;
    }

    public OfflineGetRequest<T> saveDownloadedDataToStorage(boolean save) {
        this.saveDownloadedDataToStorage = save;
        return this;
    }

    @Override
    public Response<T> send() {
        switch (mode) {
            case ONLINE:
                return doOnlineRequest(getRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
            case LOCAL_WHEN_ONLINE_FAILED:
                Response<T> onlineResponse = doOnlineRequest(getRequest, cleanStorageOnSuccessDownload, saveDownloadedDataToStorage);
                if (onlineResponse.isSuccess()) {
                    return onlineResponse;
                } else {
                    return doLocalRequest(getRequest);
                }
            case LOCAL_ONLINE_IN_BACKGROUND:
                doInBackground(getRequest);
                return doLocalRequest(getRequest);
            case LOCAL:
                return doLocalRequest(getRequest);
        }

        return null;
    }

    public abstract Response<T> doOnlineRequest(ResultRequest<T> getRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage);

    public abstract Response<T> doLocalRequest(ResultRequest<T> getRequest);
}
