package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

import java.util.List;

public class Page<T> {

    @SyncanoField(name = "prev", readOnly = true)
    private String prev;

    @SyncanoField(name = "objects", readOnly = true)
    private List<T> objects;

    @SyncanoField(name = "next", readOnly = true)
    private String next;

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public List<T> getObjects() {
        return objects;
    }

    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
