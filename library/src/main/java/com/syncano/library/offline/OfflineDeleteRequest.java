package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.data.SyncanoObject;

public class OfflineDeleteRequest<T extends SyncanoObject> extends OfflineRequest<T> {

    public OfflineDeleteRequest(RequestDelete<T> request) {
        super(request);
        SyncanoClass clazzAnnotation = (SyncanoClass) request.getResultType().getAnnotation(SyncanoClass.class);
        mode(clazzAnnotation.saveMode());
    }

    @Override
    public Response<T> doOnlineRequest(ResultRequest<T> onlineRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage) {
        RequestDelete<T> request = (RequestDelete<T>) onlineRequest;
        Response<T> onlineResponse = request.send();
        if (onlineResponse.isSuccess()) {
            Context ctx = getSyncano().getAndroidContext();
            Class<? extends SyncanoObject> type = request.getResultType();
            if (cleanStorageOnSuccessDownload) {
                OfflineHelper.clearTable(ctx, type);
            }
            if (saveDownloadedDataToStorage) {
                OfflineHelper.deleteObject(ctx, request.getResultType(), request.getId());
            }
        }
        return onlineResponse;
    }

    @Override
    public Response<T> doLocalRequest(ResultRequest<T> onlineRequest) {
        RequestDelete<T> request = (RequestDelete<T>) onlineRequest;
        Context ctx = getSyncano().getAndroidContext();
        boolean success = OfflineHelper.deleteObject(ctx, request.getResultType(), request.getId());
        Response<T> response = new Response<>();
        response.setDataFromLocalStorage(true);
        response.setResultCode(success ? Response.CODE_SUCCESS : Response.CODE_UNKNOWN_ERROR);
        return response;
    }


}
