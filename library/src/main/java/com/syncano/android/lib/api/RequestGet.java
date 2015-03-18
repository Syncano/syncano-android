package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.utils.SyncanoHttpClient;

import java.util.HashMap;
import java.util.Map;

public class RequestGet<T> extends SimpleRequest<T> {

    private FieldsFilter fieldsFilter;

    public RequestGet(Class<T> responseType, String url, Syncano syncano) {
        super(responseType, url, syncano);
    }

    @Override
    public String getRequestMethod() {
        return SyncanoHttpClient.METHOD_GET;
    }

    @Override
    public Map<String, String> prepareUrlParams() {
        Map<String, String> urlParams = super.prepareUrlParams();

        if (urlParams == null) {
            urlParams = new HashMap<>();
        }

        if (fieldsFilter != null && fieldsFilter.getFieldNames() != null && fieldsFilter.getFieldNames().size() > 0) {

            StringBuilder filterFields = new StringBuilder();
            for (String fieldName : fieldsFilter.getFieldNames()) {
                if (filterFields.length() != 0) filterFields.append(',');
                filterFields.append(fieldName);
            }

            urlParams.put(fieldsFilter.getFilterTypeString(), filterFields.toString());
        }

        return urlParams;
    }

    public void setFieldsFilter(FieldsFilter fieldsFilter) {
        this.fieldsFilter = fieldsFilter;
    }
}
