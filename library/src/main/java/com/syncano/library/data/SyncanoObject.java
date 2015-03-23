package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

public abstract class SyncanoObject extends Entity {

    private static final String FIELD_REVISION = "revision";

    @SyncanoField(name = FIELD_REVISION, readOnly = true)
    private int revision;

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }
}
