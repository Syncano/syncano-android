package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

public class Group {
    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private int id;

    @SyncanoField(name = FIELD_NAME, required = true)
    private String name;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
