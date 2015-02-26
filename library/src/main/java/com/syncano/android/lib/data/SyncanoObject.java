package com.syncano.android.lib.data;

import com.syncano.android.lib.annotation.SyncanoField;

import java.util.Date;

public abstract class SyncanoObject {

    private static final String FIELD_ID = "id";
    private static final String FIELD_CREATED_AT = "created_at";
    private static final String FIELD_UPDATED_AT = "updated_at";
    private static final String FIELD_REVISION = "revision";

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private int id;

    @SyncanoField(name = FIELD_CREATED_AT, readOnly = true)
    private Date createdAt;

    @SyncanoField(name = FIELD_UPDATED_AT, readOnly = true)
    private Date updatedAt;

    @SyncanoField(name = FIELD_REVISION, readOnly = true)
    private int revision;

    public int getId() {
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

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
