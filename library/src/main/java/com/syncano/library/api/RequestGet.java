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

        if (fieldsFilter != null && fieldsFilter.getFieldNames() != null && fieldsFilter.getFieldNames().size() > 0) {

            StringBuilder filterFields = new StringBuilder();
            for (String fieldName : fieldsFilter.getFieldNames()) {
                if (filterFields.length() != 0) filterFields.append(',');
                filterFields.append(fieldName);
            }

            addUrlParam(fieldsFilter.getFilterTypeString(), filterFields.toString());
        }
    }

    public void setFieldsFilter(FieldsFilter fieldsFilter) {
        this.fieldsFilter = fieldsFilter;
    }
}
