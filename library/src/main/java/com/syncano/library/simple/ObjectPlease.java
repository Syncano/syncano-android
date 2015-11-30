package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class ObjectPlease<T extends SyncanoObject> {
    private Syncano syncano;
    private Class<T> clazz;
    private String sortByField;
    private boolean sortByReversed = false;
    private int limit = -1;
    private Where where;

    public ObjectPlease(Class<T> clazz) {
        this.clazz = clazz;
        this.syncano = Syncano.getInstance();
    }

    public Response<List<T>> get() {
        RequestGetList<T> request = syncano.getObjects(clazz);
        decorateRequest(request);
        return request.send();
    }

    public Response<T> get(int id) {
        return syncano.getObject(clazz, id).send();
    }

    private void decorateRequest(RequestGetList<T> request) {
        if (sortByField != null) {
            request.setOrderBy(sortByField, sortByReversed);
        }
        if (limit != -1) {
            request.setLimit(limit);
        }
        if (where != null) {
            request.setWhereFilter(where);
        }
    }

    public ObjectPlease<T> on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public ObjectPlease<T> sortBy(String fieldName) {
        sortByField = fieldName;
        sortByReversed = false;
        return this;
    }

    public ObjectPlease<T> sortByReversed(String fieldName) {
        sortByField = fieldName;
        sortByReversed = true;
        return this;
    }

    public ObjectPlease<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Where where() {
        where = new Where(this);
        return where;
    }

}