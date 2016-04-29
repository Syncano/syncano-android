package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

import java.util.Date;

public abstract class Entity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_UPDATED_AT = "updated_at";

    @SyncanoField(name = FIELD_ID, readOnly = true, inSchema = false)
    private Integer id;

    @SyncanoField(name = FIELD_CREATED_AT, readOnly = true, inSchema = false)
    private Date createdAt;

    @SyncanoField(name = FIELD_UPDATED_AT, readOnly = true, inSchema = false)
    private Date updatedAt;

    public Entity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isDirty() {
        return id == null || createdAt == null;
    }
}
