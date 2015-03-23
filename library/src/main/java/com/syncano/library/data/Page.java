package com.syncano.library.data;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Page<T> {

    @Expose(deserialize = true, serialize = false)
    private String prev;

    @Expose(deserialize = true, serialize = false)
    private List<T> objects;

    @Expose(deserialize = true, serialize = false)
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
