package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.choice.FilterType;
import com.syncano.library.utils.SyncanoHttpClient;

import java.util.List;

public class RequestGet<T> extends ResultRequest<T> {

    private FieldsFilter fieldsFilter;
    private Integer requestedId;

    public RequestGet(Class<T> resultType, String url, Syncano syncano, int id) {
        this(resultType, url, syncano);
        requestedId = id;
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
    }

    public RequestGet(Class<T> resultType, String url, Syncano syncano) {
        super(resultType, url, syncano);
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
    }

    public RequestGet(T dataObject, String url, Syncano syncano) {
        super(dataObject, url, syncano);
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
    }

    public RequestGet(String url, Syncano syncano) {
        super(url, syncano);
        addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_GET;
    }

    @Override
    public void prepareUrlParams() {
        super.prepareUrlParams();
        if (isFieldsFilter()) {
            addUrlParam(fieldsFilter.getFilterTypeString(), createFilterFieldParam());
        }
    }

    public Integer getRequestedId() {
        return requestedId;
    }

    private String createFilterFieldParam() {
        StringBuilder filterFields = new StringBuilder();
        for (String fieldName : fieldsFilter.getFieldNames()) {
            if (filterFields.length() != 0) filterFields.append(',');
            filterFields.append(fieldName);
        }
        return filterFields.toString();
    }

    private boolean isFieldsFilter() {
        return fieldsFilter != null && fieldsFilter.getFieldNames() != null && fieldsFilter.getFieldNames().size() > 0;
    }

    public RequestGet<T> setFieldsFilter(FieldsFilter fieldsFilter) {
        this.fieldsFilter = fieldsFilter;
        return this;
    }

    public RequestGet<T> selectFields(FilterType filterType, String... fields) {
        this.fieldsFilter = new FieldsFilter(filterType, fields);
        return this;
    }

    public RequestGet<T> selectFields(FilterType filterType, List<String> fields) {
        this.fieldsFilter = new FieldsFilter(filterType, fields);
        return this;
    }
}
