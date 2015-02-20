package com.syncano.android.lib.api;

import com.syncano.android.lib.annotation.SyncanoParam;

public abstract class BaseGetMethod <T> extends BaseApiMethod<T> {

    @SyncanoParam(name = "page_size")
    private int pageSize;

    @SyncanoParam(name = "order_by")
    private String orderBy;

    @Override
    protected final String getRequestMethod() {
        return METHOD_GET;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
