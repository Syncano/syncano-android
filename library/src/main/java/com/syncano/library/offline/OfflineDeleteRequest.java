package com.syncano.library.offline;


import com.syncano.library.Syncano;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.Response;

/**
 * Wrapper for a RequestDelete, that adds offline storage function
 *
 * @param <T> Type of objects to delete
 */
public class OfflineDeleteRequest<T> extends OfflineRequest<T> {

    private RequestDelete<T> deleteRequest;

    public OfflineDeleteRequest(Syncano syncano, RequestDelete<T> deleteRequest) {
        super(syncano);
        this.deleteRequest = deleteRequest;
    }

    @Override
    public Response<T> send() {
        return null;
    }
}
