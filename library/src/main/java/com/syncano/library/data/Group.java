package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

public class Group {
    public static final String FIELD_ID = "id";
    public static final String FIELD_LABEL = "label";
    public static final String FIELD_DESCRIPTION = "description";

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private int id;

    @SyncanoField(name = FIELD_LABEL, required = true)
    private String label;

    @SyncanoField(name = FIELD_DESCRIPTION)
    private String description;

    public Group() {
    }

    public Group(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
