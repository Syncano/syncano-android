package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.FilterType;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoObject;

import java.util.List;


public class ObjectPlease<T extends SyncanoObject> {
    private Syncano syncano;
    private Class<T> clazz;
    private String sortByField;
    private SortOrder sortOrder;
    private int limit = -1;
    private Where<T> where;
    private FieldsFilter fieldsFilter;
    private String pageUrl;


    public ObjectPlease(Class<T> clazz) {
        this.clazz = clazz;
        this.syncano = Syncano.getInstance();
    }

    public ResponseGetList<T> get() {
        return prepareGetRequest().send();
    }

    public void getAsync(SyncanoCallback<List<T>> callback) {
        prepareGetRequest().sendAsync(callback);
    }

    private RequestGetList<T> prepareGetRequest() {
        RequestGetList<T> request;
        if (pageUrl == null) {
            request = syncano.getObjects(clazz);
        } else {
            request = syncano.getObjects(clazz, pageUrl);
        }
        decorateRequest(request);
        return request;
    }

    public Response<T> get(int id) {
        return syncano.getObject(clazz, id).send();
    }

    public void getAsync(int id, SyncanoCallback<T> callback) {
        syncano.getObject(clazz, id).sendAsync(callback);
    }

    private void decorateRequest(RequestGetList<T> request) {
        if (sortByField != null) {
            request.setOrderBy(sortByField, sortOrder);
        }
        if (limit != -1) {
            request.setLimit(limit);
        }
        if (where != null) {
            request.setWhereFilter(where);
        }
        if (fieldsFilter != null) {
            request.setFieldsFilter(fieldsFilter);
        }
    }

    public ObjectPlease<T> on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public ObjectPlease<T> orderBy(String fieldName) {
        return orderBy(fieldName, SortOrder.ASCENDING);

    }

    public ObjectPlease<T> orderBy(String fieldName, SortOrder sortOrder) {
        this.sortByField = fieldName;
        this.sortOrder = sortOrder;
        return this;
    }

    public ObjectPlease<T> selectFields(FilterType filterType, String... fields) {
        this.fieldsFilter = new FieldsFilter(filterType, fields);
        return this;
    }

    public ObjectPlease<T> selectFields(FilterType filterType, List<String> fields) {
        this.fieldsFilter = new FieldsFilter(filterType, fields);
        return this;
    }

    public ObjectPlease<T> setFieldsFilter(FieldsFilter fieldsFilter) {
        this.fieldsFilter = fieldsFilter;
        return this;
    }

    public ObjectPlease<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public ObjectPlease<T> page(String pageUrl) {
        this.pageUrl = pageUrl;
        return this;
    }

    public Where<T> where() {
        where = new Where<>(this);
        return where;
    }
}
