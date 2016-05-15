package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.data.SyncanoObject;

import java.util.Collections;

public class OfflineFetchRequest<T extends SyncanoObject> extends OfflineRequest<T> {

    public OfflineFetchRequest(RequestGet<T> request) {
        super(request);
        SyncanoClass clazzAnnotation = (SyncanoClass) request.getResultType().getAnnotation(SyncanoClass.class);
        mode(clazzAnnotation.getMode());
    }

    @Override
    public Response<T> doOnlineRequest(ResultRequest<T> request, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage) {
        Response<T> onlineResponse = request.send();
        if (onlineResponse.isSuccess()) {
            Context ctx = getSyncano().getAndroidContext();
            Class<? extends SyncanoObject> type = request.getResultType();
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
    public Response<T> doLocalRequest(ResultRequest<T> request) {
        RequestGet<T> getRequest = (RequestGet<T>) request;
        SyncanoObject data;
        if (getRequest.getResultObject() != null && getRequest.isSetUpdateGivenObject()) {
            data = OfflineHelper.readObject(getSyncano().getAndroidContext(), getRequest.getResultObject());
        } else {
            data = OfflineHelper.readObject(getSyncano().getAndroidContext(), getRequest.getResultType(), getRequest.getRequestedId());
        }
        Response<T> response = new Response<>();
        response.setData((T) data).setDataFromLocalStorage(true).setResultCode(Response.CODE_SUCCESS);
        return response;
    }


}
