package com.syncano.library.offline;

import android.content.Context;
import android.util.Log;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.api.SendRequest;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;

public class OfflineSaveRequest<T extends SyncanoObject> extends OfflineRequest<T> {

    public OfflineSaveRequest(SendRequest<T> request) {
        super(request);
        SyncanoClass clazzAnnotation = (SyncanoClass) request.getResultType().getAnnotation(SyncanoClass.class);
        mode(clazzAnnotation.saveMode());
        // not clear db afters saving object, even if set on class annotation
        cleanStorageOnSuccessDownload(false);
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
        SyncanoObject data = request.getResultObject();
        if (data == null) {
            throw new RuntimeException("Trying to save null");
        }
        // checking if fields to increment
        if (data.getIncrementBuilder().hasAdditionFields()) {
            Log.e(OfflineSaveRequest.class.getSimpleName(), "Incrementing fields doesn't work on local storage calls");
        }
        // check if fields to clear
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(data.getClass());
        for (Field f : fields) {
            if (data.isOnClearList(SyncanoClassHelper.getFieldName(f))) {
                f.setAccessible(true);
                try {
                    f.set(data, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // shouldn't ever happen
                }
            }
        }
        if (data.getId() == null || data.getId() == 0) {
            data.setId(null);
        }
        // writing
        Context ctx = getSyncano().getAndroidContext();
        OfflineHelper.writeObjects(ctx, Collections.singletonList(data), request.getResultType());
        Response<T> response = new Response<>();
        response.setData((T) data).setDataFromLocalStorage(true).setResultCode(Response.CODE_SUCCESS);
        return response;
    }

    @Override
    public void afterRequests(ResultRequest<T> onlineRequest) {
        SyncanoObject data = onlineRequest.getResultObject();
        if (data != null) {
            data.resetRequestBuildingFields();
        }
    }
}
