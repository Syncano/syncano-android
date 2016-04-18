package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.FieldsFilter;
import com.syncano.library.api.RequestCount;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.RequestTemplate;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.FilterType;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineMode;
import com.syncano.library.offline.OfflineGetListRequest;

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
    private String dataEndpoint;
    private boolean estimateCount = false;
    private boolean getAll = false;
    private OfflineMode mode = OfflineMode.ONLINE;
    private boolean cleanStorageOnSuccessDownload = false;
    private boolean saveDownloadedDataToStorage = false;
    private SyncanoCallback<List<T>> backgroundCallback;

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
        return prepareOfflineRequest().send();
    }

    /**
     * You can get limited amount of objects in one request. This method gets objects until all are
     * downloaded. Use carefully. Will work very bad for more than a few hundreds of objects .
     */
    @Deprecated
    public ResponseGetList<T> getAll() {
        getAll(true);
        return prepareOfflineRequest().send();
    }

    /**
     * Same as getAll(), but asynchronously
     *
     * @param callback callback
     */
    @Deprecated
    public void getAll(SyncanoCallback<List<T>> callback) {
        getAll(true);
        prepareOfflineRequest().sendAsync(callback);
    }

    /**
     * Same as get(), but asynchronously
     *
     * @param callback callback
     */
    public void get(SyncanoCallback<List<T>> callback) {
        prepareOfflineRequest().sendAsync(callback);
    }

    /**
     * Does a requests for a list of objects. Processes them with given template.
     *
     * @param templateName name of a template
     * @return Processed result
     */
    public Response<String> getWithTemplate(String templateName) {
        return prepareTemplateRequest(templateName).send();
    }

    /**
     * Does a requests for a list of objects. Processes them with given template.
     *
     * @param templateName name of a template
     * @param callback     Callback that will be called after finished request
     */
    public void getWithTemplate(String templateName, SyncanoCallback<String> callback) {
        prepareTemplateRequest(templateName).sendAsync(callback);
    }

    public RequestGetList<T> prepareGetRequest() {
        RequestGetList<T> request = instantiateRequestGetList();
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
        request.setGetAll(getAll);
        return request;
    }

    public OfflineGetListRequest<T> prepareOfflineRequest() {
        OfflineGetListRequest<T> request = new OfflineGetListRequest<>(prepareGetRequest());
        request.mode(mode);
        request.cleanStorageOnSuccessDownload(cleanStorageOnSuccessDownload);
        request.saveDownloadedDataToStorage(saveDownloadedDataToStorage);
        request.setBackgroundCallback(backgroundCallback);
        return request;
    }

    public RequestTemplate prepareTemplateRequest(String templateName) {
        return new RequestTemplate(prepareGetRequest(), templateName);
    }

    private RequestGetList<T> instantiateRequestGetList() {
        RequestGetList<T> request;
        if (pageUrl != null) {
            request = syncano.getObjects(clazz, pageUrl);
        } else if (dataEndpoint == null) {
            request = syncano.getObjects(clazz);
        } else {
            request = syncano.getObjectsDataEndpoint(clazz, dataEndpoint);
        }
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

    public RequestBuilder<T> getAll(boolean getAll) {
        this.getAll = getAll;
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

    /**
     * Gets data from given table view
     *
     * @param viewName Virtual table name
     * @return itself
     */
    @Deprecated
    public RequestBuilder<T> tableView(String viewName) {
        return dataEndpoint(viewName);
    }

    /**
     * Gets data from given data endpoint
     *
     * @param name data endpoint name
     * @return itself
     */
    public RequestBuilder<T> dataEndpoint(String name) {
        this.dataEndpoint = name;
        return this;
    }

    public RequestBuilder<T> mode(OfflineMode mode) {
        this.mode = mode;
        return this;
    }

    public RequestBuilder<T> cleanStorageOnSuccessDownload(boolean clean) {
        this.cleanStorageOnSuccessDownload = clean;
        return this;
    }

    public RequestBuilder<T> saveDownloadedDataToStorage(boolean save) {
        this.saveDownloadedDataToStorage = save;
        return this;
    }

    public RequestBuilder<T> backgroundCallback(SyncanoCallback<List<T>> callback) {
        this.backgroundCallback = callback;
        return this;
    }
}
