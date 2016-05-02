package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.api.SendRequest;
import com.syncano.library.data.SyncanoObject;

import java.util.Collections;

/**
 * Wrapper for a RequestGet, that adds offline storage function
 *
 * @param <T> Type of object to get
 */
public class OfflineGetOneRequest<T extends SyncanoObject> extends OfflineGetRequest<T> {

    public OfflineGetOneRequest(ResultRequest<T> getRequest) {
        super(getRequest);
    }

    @Override
    public Response<T> doOnlineRequest(ResultRequest<T> getRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage) {
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
    public Response<T> doLocalRequest(ResultRequest<T> request) {
        if (request instanceof SendRequest) {
            return localSaveRequest((SendRequest<T>) request);
        } else if (request instanceof RequestGet) {
            return localGetRequest((RequestGet<T>) request);
        } else if (request instanceof RequestDelete) {
            return localDeleteRequest((RequestDelete<T>) request);
        }
        throw new RuntimeException("Unsupported type of request " + request.getClass().getSimpleName());
    }

    private Response<T> localDeleteRequest(RequestDelete<T> request) {
        Context ctx = getSyncano().getAndroidContext();
        boolean success = OfflineHelper.deleteObject(ctx, request.getResultType(), request.getId());
        Response<T> response = new Response<>();
        response.setDataFromLocalStorage(true);
        response.setResultCode(success ? Response.CODE_SUCCESS : Response.CODE_UNKNOWN_ERROR);
        return response;
    }

    private Response<T> localSaveRequest(SendRequest<T> request) {
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

    private Response<T> localGetRequest(RequestGet<T> getRequest) {
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
