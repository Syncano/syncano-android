package com.syncano.android.lib.api;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.utils.SyncanoHttpClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

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
    public List<NameValuePair> prepareUrlParams() {
        List<NameValuePair> urlParams = super.prepareUrlParams();

        if (urlParams == null) {
            urlParams = new ArrayList<>();
        }

        if (fieldsFilter != null && fieldsFilter.getFieldNames() != null && fieldsFilter.getFieldNames().size() > 0) {

            StringBuilder filterFields = new StringBuilder();
            for (String fieldName : fieldsFilter.getFieldNames()) {
                if (filterFields.length() != 0) filterFields.append(',');
                filterFields.append(fieldName);
            }

            urlParams.add(new BasicNameValuePair(fieldsFilter.getFilterTypeString(), filterFields.toString()));
        }

        return urlParams;
    }

    public void setFieldsFilter(FieldsFilter fieldsFilter) {
        this.fieldsFilter = fieldsFilter;
    }
}
