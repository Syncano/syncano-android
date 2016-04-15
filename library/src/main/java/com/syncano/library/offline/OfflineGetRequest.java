package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.data.SyncanoObject;

import java.util.List;


/**
 * Wrapper for a RequestGetList, that adds offline storage function
 *
 * @param <T> Type of objects to get
 */
public class OfflineGetRequest<T extends SyncanoObject> extends OfflineRequest<List<T>> {

    private RequestGetList<T> getRequest;
    private GetMode mode = GetMode.ONLINE;
    private boolean cleanStorageOnSuccessDownload = false;
    private boolean saveDownloadedDataToStorage = false;

    public OfflineGetRequest(RequestGetList<T> getRequest) {
        super(getRequest.getSyncano());
        if (!SyncanoObject.class.isAssignableFrom(getRequest.getResultType())) {
            throw new RuntimeException("Using offline storage is only possible for SyncanoObject objects");
        }
        this.getRequest = getRequest;
    }

    public OfflineGetRequest<T> mode(GetMode mode) {
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
    public ResponseGetList<T> send() {
        switch (mode) {
            case ONLINE:
                return doOnlineRequest();
            case LOCAL_WHEN_ONLINE_FAILED:
                ResponseGetList<T> onlineResponse = doOnlineRequest();
                if (onlineResponse.isSuccess()) {
                    return onlineResponse;
                } else {
                    return doLocalRequest();
                }
            case LOCAL_ONLINE_IN_BACKGROUND:
                doInBackground(getRequest);
                return doLocalRequest();
            case LOCAL:
                return doLocalRequest();
        }

        return null;
    }

    private ResponseGetList<T> doOnlineRequest() {
        ResponseGetList<T> onlineResponse = getRequest.send();
        if (onlineResponse.isSuccess()) {
            Context ctx = getSyncano().getAndroidContext();
            Class<? extends SyncanoObject> type = getRequest.getResultType();
            if (cleanStorageOnSuccessDownload) {
                OfflineHelper.clearTable(ctx, type);
            }
            if (saveDownloadedDataToStorage) {
                OfflineHelper.writeObjects(ctx, onlineResponse.getData(), type);
            }
        }
        return onlineResponse;
    }

    private ResponseGetList<T> doLocalRequest() {
        List data = OfflineHelper.readObjects(getSyncano().getAndroidContext(), getRequest.getResultType(),
                getRequest.getWhereFilter(), getRequest.getOrderByParam());
        ResponseGetList<T> response = new ResponseGetList<>(getSyncano(), getRequest.getResultType());
        response.setData(data).setDataFromLocalStorage(true).setResultCode(Response.CODE_SUCCESS);
        return response;
    }
}
