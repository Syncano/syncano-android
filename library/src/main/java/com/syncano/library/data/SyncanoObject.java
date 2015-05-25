package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

public abstract class SyncanoObject extends Entity {

    private static final String FIELD_REVISION = "revision";
    private static final String FIELD_EXPECTED_REVISION = "expected_revision";

    @SyncanoField(name = FIELD_REVISION, readOnly = true)
    private int revision;

    @SyncanoField(name = FIELD_EXPECTED_REVISION)
    private int expectedRevision;

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getExpectedRevision() {
        return expectedRevision;
    }

    public void setExpectedRevision(int expectedRevision) {
        this.expectedRevision = expectedRevision;
    }
}
