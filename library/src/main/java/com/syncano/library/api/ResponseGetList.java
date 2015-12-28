package com.syncano.library.api;

import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;

import java.util.List;

public class ResponseGetList<T> extends Response<List<T>> {
    /*
    This class was made to make easier calls for next pages of objects.
    It would be able to make this calls setting three values lastPk, lastValue, direction,
    but it would be hard for the user to set all of them properly. Also hard to implement passing
    last value in a right format. That's why it was decided to use links returned from server.
     */

    private String previousPageUrl;
    private String nextPageUrl;
    private Integer estimatedCount;
    private Syncano syncano;
    private Class<T> resultType;

    public ResponseGetList(Syncano syncano, Class<T> resultType) {
        this.syncano = syncano;
        this.resultType = resultType;
    }

    public String getPreviousPageUrl() {
        return previousPageUrl;
    }

    public void setPreviousPageUrl(String previousPageUrl) {
        this.previousPageUrl = previousPageUrl;
    }

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
        RequestGetList<T> req = new RequestGetList<>(resultType, nextPageUrl, syncano);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
    }

    public RequestGetList<T> getPreviousPageRequest() {
        RequestGetList<T> req = new RequestGetList<>(resultType, previousPageUrl, syncano);
        req.addCorrectHttpResponseCode(Response.HTTP_CODE_SUCCESS);
        return req;
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
}
