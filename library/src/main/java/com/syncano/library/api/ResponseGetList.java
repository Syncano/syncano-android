package com.syncano.library.api;

import java.util.List;

public class ResponseGetList<T> extends Response<List<T>> {
    /*
    This class was made to make easier calls for next pages of objects.
    It would be able to make this calls setting three values lastPk, lastValue, direction,
    but it would be hard for the user to set all of them properly. Also hard to implement passing
    last value in a right format. That's why it was decided to use links returned from server.
     */

    private String linkPrevious;
    private String linkNext;

    public String getLinkPrevious() {
        return linkPrevious;
    }

    public void setLinkPrevious(String linkPrevious) {
        this.linkPrevious = linkPrevious;
    }

    public String getLinkNext() {
        return linkNext;
    }

    public void setLinkNext(String linkNext) {
        this.linkNext = linkNext;
    }
}
