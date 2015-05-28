package com.syncano.library.api;


import com.google.gson.JsonElement;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.data.PageInternal;


import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class RequestGetList<T> extends RequestGet<List<T>> {

    private static final int DIRECTION_NEXT = 1;
    private static final int DIRECTION_PREV = 0;

    protected Class<T> resultType;
    private Where where;
    private String orderBy;
    private int pageSize = 0;

    private int lastPk = 0;
    private int direction = DIRECTION_NEXT;

    public RequestGetList(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
    }

    @Override
    public void prepareUrlParams() {
        super.prepareUrlParams();

        if (where != null) {
            addUrlParam(Constants.URL_PARAM_QUERY, where.buildQuery());
        }

        if (orderBy != null) {
            addUrlParam(Constants.URL_PARAM_ORDER_BY, orderBy);
        }

        if (pageSize > 0) {
            addUrlParam(Constants.URL_PARAM_PAGE_SIZE, String.valueOf(pageSize));
        }

        if (lastPk > 0) {
            addUrlParam(Constants.URL_PARAM_PAGE_LAST_PK, String.valueOf(lastPk));
            addUrlParam(Constants.URL_PARAM_PAGE_DIRECTION, String.valueOf(direction));
        }
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

    /**
     * Set last pk for paging.
     * If revert is false, "next page" will be requested.
     * When reverted, direction will be changed and objects with
     * smaller id will be get. It's equivalent of "previous page".
     * @param lastPk Id to start from paging.
     * @param revertDirection If true, page direction will be changed.
     */
    public void setLastPk(int lastPk, boolean revertDirection) {
        this.lastPk = lastPk;

        if (revertDirection) {
            direction = DIRECTION_PREV;
        } else {
            direction = DIRECTION_NEXT;
        }
    }
}
