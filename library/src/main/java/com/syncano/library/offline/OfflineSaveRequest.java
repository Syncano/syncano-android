package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.api.SendRequest;
import com.syncano.library.data.SyncanoObject;

import java.util.Collections;

public class OfflineSaveRequest<T extends SyncanoObject> extends OfflineRequest<T> {

    public OfflineSaveRequest(SendRequest<T> request) {
        super(request);
        SyncanoClass clazzAnnotation = (SyncanoClass) request.getResultType().getAnnotation(SyncanoClass.class);
        mode(clazzAnnotation.saveMode());
    }

    @Override
    public Response<T> doOnlineRequest(ResultRequest<T> request, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage) {
        Response<T> onlineResponse = request.send();
        if (onlineResponse.isSuccess()) {
            Context ctx = getSyncano().getAndroidContext();
            Class<? extends SyncanoObject> type = request.getResultType();
            if (saveDownloadedDataToStorage) {
                OfflineHelper.writeObjects(ctx, Collections.singletonList(onlineResponse.getData()), type);
            }
        }
        return onlineResponse;
    }

    @Override
    public Response<T> doLocalRequest(ResultRequest<T> request) {
        SyncanoObject data = request.getResultObject();
        if (data == null) {
            throw new RuntimeException("Trying to save null");
        }
        if (data.getId() == null || data.getId() == 0) {
            throw new RuntimeException("Saving objects without id not yet supported");
            //TODO implement local saving objects without id
            /*
            Example. Object without id is saved in db. Then background online call returns data with ids.
            Then it should find related objects and update their params in db instead of creating new ones.
            Maybe add some additional temporary id? It will be saved in db and kept in objects that
            are being processed by online call. Then OfflineHelper may be able to connect them.
            */
        }
        Context ctx = getSyncano().getAndroidContext();
        OfflineHelper.writeObjects(ctx, Collections.singletonList(data), request.getResultType());
        Response<T> response = new Response<>();
        response.setData((T) data).setDataFromLocalStorage(true).setResultCode(Response.CODE_SUCCESS);
        return response;
    }


}
