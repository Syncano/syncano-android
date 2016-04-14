package com.syncano.library.api;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.PageInternal;
import com.syncano.library.parser.GsonParser;

import java.util.ArrayList;
import java.util.List;

public class RequestGetList<T> extends RequestGet<List<T>> {

    protected Class<T> resultType;
    private Where where;
    private String orderBy;
    private Integer pageSize;
    private boolean estimateCount;
    private Gson gson;
    private boolean getAll = false;

    public RequestGetList(Class<T> resultType, String url, Syncano syncano) {
        super(url, syncano);
        this.resultType = resultType;
        gson = GsonParser.createGson(resultType);
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

        if (pageSize != null) {
            addUrlParam(Constants.URL_PARAM_PAGE_SIZE, String.valueOf(pageSize));
        }

        if (estimateCount) {
            addUrlParam(Constants.URL_PARAM_INCLUDE_COUNT, Boolean.toString(true));
        }
    }

    @Override
    public List<T> parseResult(Response<List<T>> response, String json) {
        PageInternal pageInternal = gson.fromJson(json, PageInternal.class);
        List<T> resultList = new ArrayList<>();

        List<JsonElement> objects = pageInternal.getObjects();
        if (objects != null) {
            for (JsonElement element : objects) {
                resultList.add(gson.fromJson(element, resultType));
            }
        }

        ResponseGetList<T> r = (ResponseGetList<T>) response;
        r.setNextPageUrl(pageInternal.getNext());
        r.setPreviousPageUrl(pageInternal.getPrev());
        r.setEstimatedCount(pageInternal.getCount());
        return resultList;
    }

    @Override
    public Class getResultType() {
        return resultType;
    }

    /**
     * Filter your objects using query parameter.
     *
     * @param where Filtering query.
     */
    public RequestGetList<T> setWhereFilter(Where where) {
        this.where = where;
        return this;
    }

    /**
     * @return Filtering query
     */
    public Where getWhereFilter() {
        return where;
    }

    /**
     * @return Order by param value
     */
    public String getOrderByParam() {
        return orderBy;
    }

    /**
     * You can order objects using this method. Field must be marked as "order_index" in class schema.
     *
     * @param fieldName Field to order by.
     * @param sortOrder {@link SortOrder#DESCENDING} data will be sorted descending
     *                  {@link SortOrder#ASCENDING} data will be sorted ascending
     * @see SortOrder
     */
    public RequestGetList<T> setOrderBy(String fieldName, SortOrder sortOrder) {
        if (fieldName == null || fieldName.length() == 0 || fieldName.startsWith("-"))
            throw new RuntimeException("Syncano field name can not be empty or begin with character '-'");
        orderBy = (sortOrder == SortOrder.DESCENDING) ? "-" + fieldName : fieldName;
        return this;
    }

    /**
     * Estimate count.
     */
    public RequestGetList<T> estimateCount() {
        estimateCount = true;
        return this;
    }

    /**
     * Set limit of how many items do you want to get.
     *
     * @param limit Maximum amount of items.
     */
    public RequestGetList<T> setLimit(int limit) {
        pageSize = limit;
        return this;
    }


    @Override
    public Response<List<T>> instantiateResponse() {
        return new ResponseGetList<>(syncano, resultType);
    }

    @Override
    public ResponseGetList<T> send() {
        ResponseGetList<T> r = (ResponseGetList<T>) super.send();
        if (!getAll || !r.isSuccess()) {
            return r;
        }

        ArrayList<T> data = new ArrayList<>(r.getData());
        while (r.hasNextPage()) {
            r = r.getNextPage();
            if (!r.isSuccess()) {
                return r;
            } else {
                data.addAll(r.getData());
            }
        }

        ResponseGetList<T> response = new ResponseGetList<>(syncano, resultType);
        response.setData(data);
        response.setResultCode(Response.CODE_SUCCESS);
        response.setHttpResultCode(Response.HTTP_CODE_SUCCESS);
        return response;
    }

    /**
     * @return true if is set to download all objects instead one page
     */
    public boolean isGetAll() {
        return getAll;
    }

    /**
     * Allows to download all objects in a loop. Use very carefully.
     *
     * @param getAll download all objects, not only single page
     */
    public RequestGetList<T> setGetAll(boolean getAll) {
        this.getAll = getAll;
        return this;
    }
}
