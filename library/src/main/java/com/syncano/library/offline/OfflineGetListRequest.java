package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.data.SyncanoObject;

import java.util.List;


/**
 * Wrapper for a RequestGetList, that adds offline storage function
 *
 * @param <T> Type of objects to get
 */
public class OfflineGetListRequest<T> extends OfflineGetRequest<List<T>> {

    public OfflineGetListRequest(RequestGetList<T> getRequest) {
        super(getRequest);
    }

    @Override
    public ResponseGetList<T> doOnlineRequest(ResultRequest<List<T>> getRequest, boolean cleanStorageOnSuccessDownload, boolean saveDownloadedDataToStorage) {
        ResponseGetList<T> onlineResponse = (ResponseGetList<T>) getRequest.send();
        if (onlineResponse.isSuccess()) {
            Context ctx = getSyncano().getAndroidContext();
            Class<? extends SyncanoObject> type = getRequest.getResultType();
            if (cleanStorageOnSuccessDownload) {
                OfflineHelper.clearTable(ctx, type);
            }
            if (saveDownloadedDataToStorage) {
                OfflineHelper.writeObjects(ctx, (List<? extends SyncanoObject>) onlineResponse.getData(), type);
            }
        }
        return onlineResponse;
    }

    @Override
    public ResponseGetList<T> doLocalRequest(ResultRequest<List<T>> getRequest) {
        RequestGetList getListRequest = (RequestGetList) getRequest;
        List data = OfflineHelper.readObjects(getSyncano().getAndroidContext(), getListRequest.getResultType(),
                getListRequest.getWhereFilter(), getListRequest.getOrderByParam());
        ResponseGetList<T> response = new ResponseGetList<>(getSyncano(), getListRequest.getResultType());
        response.setData(data).setDataFromLocalStorage(true).setResultCode(Response.CODE_SUCCESS);
        return response;
    }
}
