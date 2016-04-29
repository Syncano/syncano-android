package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;

import java.util.Collections;

/**
 * Wrapper for a RequestGet, that adds offline storage function
 *
 * @param <T> Type of object to get
 */
public class OfflineGetOneRequest<T extends SyncanoObject> extends OfflineGetRequest<T> {

    public OfflineGetOneRequest(RequestGet<T> getRequest) {
        super(getRequest);
    }

    @Override
    public Response<T> doOnlineRequest(RequestGet<T> getRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage) {
        Response<T> onlineResponse = getRequest.send();
        if (onlineResponse.isSuccess()) {
            Context ctx = getSyncano().getAndroidContext();
            Class<? extends SyncanoObject> type = getRequest.getResultType();
            if (cleanStorageOnSuccessDownload) {
                OfflineHelper.clearTable(ctx, type);
            }
            if (saveDownloadedDataToStorage) {
                OfflineHelper.writeObjects(ctx, Collections.singletonList(onlineResponse.getData()), type);
            }
        }
        return onlineResponse;
    }

    @Override
    public Response<T> doLocalRequest(RequestGet<T> getRequest) {
        SyncanoObject data;
        if (getRequest.getResultObject() != null) {
            data = OfflineHelper.readObject(getSyncano().getAndroidContext(), getRequest.getResultObject());
        } else {
            data = OfflineHelper.readObject(getSyncano().getAndroidContext(), getRequest.getResultType(), getRequest.getRequestedId());
        }
        Response<T> response = new Response<>();
        response.setData((T) data).setDataFromLocalStorage(true).setResultCode(Response.CODE_SUCCESS);
        return response;
    }
}
