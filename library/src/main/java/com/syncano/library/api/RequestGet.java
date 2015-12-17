package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.utils.SyncanoHttpClient;

public abstract class RequestGet<T> extends Request<T> {

    private FieldsFilter fieldsFilter;

    public RequestGet(String url, Syncano syncano) {
        super(url, syncano);
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
}
