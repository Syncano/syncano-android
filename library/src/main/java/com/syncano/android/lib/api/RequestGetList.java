package com.syncano.android.lib.api;


import com.google.gson.JsonElement;
import com.syncano.android.lib.Constants;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.data.PageInternal;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RequestGetList<T> extends RequestGet<List<T>> {

    protected Class<T> resultType;
    private Where where;
    private String orderBy;
    private int pageSize = 0;

    public RequestGetList(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }

    @Override
    public List<NameValuePair> prepareUrlParams() {
        List<NameValuePair> urlParams = super.prepareUrlParams();

        if (urlParams == null) {
            urlParams = new ArrayList<>();
        }

        if (where != null) {
            urlParams.add(new BasicNameValuePair(Constants.URL_PARAM_QUERY, where.buildQuery()));
        }

        if (orderBy != null) {
            urlParams.add(new BasicNameValuePair(Constants.URL_PARAM_ORDER_BY, orderBy));
        }

        if (pageSize > 0) {
            urlParams.add(new BasicNameValuePair(Constants.URL_PARAM_PAGE_SIZE, String.valueOf(pageSize)));
        }

        return urlParams;
    }

    @Override
    public List<T> parseResult(String json) {

        PageInternal pageInternal = gson.fromJson(json, PageInternal.class);
        List<T> resultList = new ArrayList<>();

        if (pageInternal.getObjects() != null) {

            for (JsonElement element : pageInternal.getObjects()) {
                resultList.add(gson.fromJson(element, resultType));
            }
        }

        return resultList;
    }

    /**
     * Filter your objects using query parameter.
     * @param where Filtering query.
     */
    public void setWhereFilter(Where where) {
        this.where = where;
    }

    /**
     * You can order objects using this method. Field must be marked as "order_index" in class schema.
     * @param fieldName Field to order by.
     * @param reverse If true, change from ascending to descending.
     */
    public void setOrderBy(String fieldName, boolean reverse) {
        orderBy = reverse ? "-" + fieldName : fieldName;
    }

    /**
     * Set limit of how many items do you want to get.
     * @param limit Maximum amount of items.
     */
    public void setLimit(int limit) {
        pageSize = limit;
    }
}
