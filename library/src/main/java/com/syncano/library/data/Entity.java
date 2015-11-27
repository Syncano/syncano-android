package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

import java.util.Date;

public abstract class Entity {

    public static final String FIELD_ID = "id";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_UPDATED_AT = "updated_at";

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private Integer id;

    @SyncanoField(name = FIELD_CREATED_AT, readOnly = true)
    private Date createdAt;

    @SyncanoField(name = FIELD_UPDATED_AT, readOnly = true)
    private Date updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

}
