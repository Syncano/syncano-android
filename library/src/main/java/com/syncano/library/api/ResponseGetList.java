package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;

import java.util.List;

public class ResponseGetList<T> extends Response<List<T>> {
    /*
    This class was made to make easier calls for next pages of objects.
    It would be able to make this calls setting values lastPk, lastValue, direction, where,
    but it would be hard for the user to set all of them properly. Also hard to implement passing
    last value in a right format. That's why it was decided to use links returned from server.
     */

    private String previousPageUrl;
    private String nextPageUrl;
    private Integer estimatedCount;
    private Syncano syncano;
    private Class<T> resultType;
    private Where usedQuery;

    public ResponseGetList(Syncano syncano, Class<T> resultType) {
        this.syncano = syncano;
        this.resultType = resultType;
    }

    /**
     * Use carefully. You also have to set Where query on a request to properly get page.
     */
    public String getPreviousPageUrl() {
        return previousPageUrl;
    }

    public void setPreviousPageUrl(String previousPageUrl) {
        this.previousPageUrl = previousPageUrl;
    }

    /**
     * Use carefully. You also have to set Where query on a request to properly get page.
     */
    public String getNextPageUrl() {
        return nextPageUrl;
    }

    public void setNextPageUrl(String nextPageUrl) {
        this.nextPageUrl = nextPageUrl;
    }

    public boolean hasNextPage() {
        return (nextPageUrl != null && !nextPageUrl.isEmpty());
    }

    public boolean hasPreviousPage() {
        return (previousPageUrl != null && !previousPageUrl.isEmpty());
    }

    public RequestGetList<T> getNextPageRequest() {
        RequestGetList<T> request = new RequestGetList<>(resultType, nextPageUrl, syncano);
        request.setWhereFilter(getUsedQuery());
        return request;
    }

    public RequestGetList<T> getPreviousPageRequest() {
        RequestGetList<T> request = new RequestGetList<>(resultType, previousPageUrl, syncano);
        request.setWhereFilter(getUsedQuery());
        return request;
    }

    public ResponseGetList<T> getNextPage() {
        return getNextPageRequest().send();
    }

    public void getNextPage(SyncanoCallback<List<T>> callback) {
        getNextPageRequest().sendAsync(callback);
    }

    public ResponseGetList<T> getPreviousPage() {
        return getPreviousPageRequest().send();
    }

    public void getPreviousPage(SyncanoCallback<List<T>> callback) {
        getPreviousPageRequest().sendAsync(callback);
    }

    public void setEstimatedCount(Integer count) {
        this.estimatedCount = count;
    }

    public Integer getEstimatedCount() {
        return estimatedCount;
    }

    public Where getUsedQuery() {
        return usedQuery;
    }

    public void setUsedQuery(Where usedQuery) {
        this.usedQuery = usedQuery;
    }
}
