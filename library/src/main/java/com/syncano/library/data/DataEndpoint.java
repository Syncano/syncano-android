package com.syncano.library.data;

import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Where;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoHashSet;

import java.util.Map;

public class DataEndpoint {

    public static final String FIELD_NAME = Constants.FIELD_NAME;
    public static final String FIELD_DESCRIPTION = Constants.FIELD_DESCRIPTION;
    public static final String FIELD_ORDER_BY = Constants.URL_PARAM_ORDER_BY;
    public static final String FIELD_PAGE_SIZE = Constants.URL_PARAM_PAGE_SIZE;
    public static final String FIELD_EXPAND = Constants.URL_PARAM_EXPAND;
    public static final String FIELD_QUERY = Constants.URL_PARAM_QUERY;
    public static final String FIELD_CLASS_NAME = Constants.DATA_ENDPOINT_PARAM_CLASS;
    private final Class<? extends SyncanoObject> clazz;
    @SyncanoField(name = FIELD_NAME)
    private final String name;
    @SyncanoField(name = FIELD_CLASS_NAME)
    private final String syncanoClassName;
    @SyncanoField(name = FIELD_DESCRIPTION)
    private String description;
    @SyncanoField(name = FIELD_ORDER_BY)
    private String orderBy;
    @SyncanoField(name = FIELD_PAGE_SIZE)
    private int pageSize;
    @SyncanoField(name = FIELD_EXPAND)
    private SyncanoHashSet expandSet = new SyncanoHashSet();
    @SyncanoField(name = FIELD_QUERY)
    private Map query;
    private Syncano syncano;

    public DataEndpoint(String name, Class<? extends SyncanoObject> clazz) {
        this.name = name;
        this.clazz = clazz;
        this.syncanoClassName = SyncanoClassHelper.getSyncanoClassName(clazz);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addExpandField(String fieldName) {
        expandSet.add(fieldName);
    }

    public void removeExpandField(String fieldName) {
        expandSet.remove(fieldName);
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

    public void setQuery(Where query) {
        this.query = query.getQueryMap();
    }

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public DataEndpoint on(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public RequestGetList<? extends SyncanoObject> please() {
        return getSyncano().getViewObjects(clazz, syncanoClassName);
    }

}