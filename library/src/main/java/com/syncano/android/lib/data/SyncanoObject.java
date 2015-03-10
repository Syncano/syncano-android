package com.syncano.android.lib.data;

import com.syncano.android.lib.annotation.SyncanoField;

import java.util.Date;

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
