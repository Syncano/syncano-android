package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestAll;
import com.syncano.library.api.RequestCount;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.FilterType;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoObject;

import java.util.List;


public class RequestBuilder<T extends SyncanoObject> {
    private Syncano syncano;
    private Class<T> clazz;
    private String sortByField;
    private SortOrder sortOrder;
    private Integer limit;
    private Where<T> where;
    private FieldsFilter fieldsFilter;
    private String pageUrl;
    private boolean estimateCount = false;


    public RequestBuilder(Class<T> clazz) {
        this.clazz = clazz;
        this.syncano = Syncano.getInstance();
    }

    /**
     * Does a requests for a list of objects.
     *
     * @return response with a requested list
     */
    public ResponseGetList<T> get() {
        return prepareGetRequest().send();
    }

    /**
     * You can get limited amount of objects in one request. This method gets objects until all are
     * downloaded. Use carefully. Will work very bad for more than a few hundreds of objects .
     */
    public ResponseGetList<T> getAll() {
        return (new RequestAll<>(prepareGetRequest())).send();
    }

    /**
     * Same as getAll(), but asynchronously
     *
     * @param callback callback
     */
    public void getAll(SyncanoCallback<List<T>> callback) {
        (new RequestAll<>(prepareGetRequest())).sendAsync(callback);
    }

    /**
     * Same as get(), but asynchronously
     *
     * @param callback callback
     */
    public void get(SyncanoCallback<List<T>> callback) {
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

    /**
     * Gets a single object
     *
     * @param id Id of requested object
     * @return Response that has requested object.
     */
    public Response<T> get(int id) {
        return syncano.getObject(clazz, id).send();
    }

    /**
     * Same as get(id), but asynchronously
     *
     * @param callback callback
     */
    public void get(int id, SyncanoCallback<T> callback) {
        syncano.getObject(clazz, id).sendAsync(callback);
    }

    private void decorateRequest(RequestGetList<T> request) {
        if (sortByField != null) {
            request.setOrderBy(sortByField, sortOrder);
        }
        if (limit != null) {
            request.setLimit(limit);
        }
        if (where != null) {
            request.setWhereFilter(where);
        }
        if (fieldsFilter != null) {
            request.setFieldsFilter(fieldsFilter);
        }
        if (estimateCount) {
            request.estimateCount();
        }
    }

    /**
     * Perform requests on this syncano instance. If not called, shared static instance will be used.
     *
     * @param syncano instance to use.
     * @return itself
     */
    public RequestBuilder<T> on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    /**
     * @param fieldName order by witch field, by default order ascending
     * @return itself
     */
    public RequestBuilder<T> orderBy(String fieldName) {
        return orderBy(fieldName, SortOrder.ASCENDING);

    }

    /**
     * @param fieldName order by witch field
     * @param sortOrder order direction
     * @return itself
     */
    public RequestBuilder<T> orderBy(String fieldName, SortOrder sortOrder) {
        this.sortByField = fieldName;
        this.sortOrder = sortOrder;
        return this;
    }

    /**
     * Get only selected fields
     *
     * @param filterType include or exclude
     * @param fields     field names
     * @return itself
     */
    public RequestBuilder<T> selectFields(FilterType filterType, String... fields) {
        this.fieldsFilter = new FieldsFilter(filterType, fields);
        return this;
    }

    /**
     * Get only selected fields
     *
     * @param filterType include or exclude
     * @param fields     field names
     * @return itself
     */
    public RequestBuilder<T> selectFields(FilterType filterType, List<String> fields) {
        this.fieldsFilter = new FieldsFilter(filterType, fields);
        return this;
    }

    /**
     * Get only selected fields
     *
     * @param fieldsFilter Previously prepared fields filter
     * @return itself
     */
    public RequestBuilder<T> setFieldsFilter(FieldsFilter fieldsFilter) {
        this.fieldsFilter = fieldsFilter;
        return this;
    }

    /**
     * Limit response size to given number
     *
     * @param limit maximum number of returned objects
     * @return itself
     */
    public RequestBuilder<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * If called, response will contain estimated number of all objects that match the request.
     *
     * @return itself
     */
    public RequestBuilder<T> estimateCount() {
        this.estimateCount = true;
        return this;
    }

    /**
     * Sets specific page to request.
     *
     * @param pageUrl page url
     * @return itself
     */
    public RequestBuilder<T> page(String pageUrl) {
        this.pageUrl = pageUrl;
        return this;
    }

    /**
     * After calling this you can start building your where query.
     *
     * @return new Where
     */
    public Where<T> where() {
        where = new Where<>(this);
        return where;
    }

    /**
     * @return Response with data as integer with estimated objects count
     * estimation start after 1000 objects, before it's accurate
     */
    public Response<Integer> getCountEstimation() {
        return new RequestCount(prepareGetRequest()).send();
    }

    /**
     * Same as getCountEstimation(), but asynchronously
     *
     * @param callback callback
     */
    public void getCountEstimation(SyncanoCallback<Integer> callback) {
        new RequestCount(prepareGetRequest()).sendAsync(callback);
    }
}
