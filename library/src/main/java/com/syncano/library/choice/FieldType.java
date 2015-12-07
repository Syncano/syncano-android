package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum FieldType {
    @SerializedName(value = "")
    NOT_SET,
    @SerializedName(value = "string")
    STRING,
    @SerializedName(value = "text")
    TEXT,
    @SerializedName(value = "integer")
    INTEGER,
    @SerializedName(value = "float")
    FLOAT,
    @SerializedName(value = "boolean")
    BOOLEAN,
    @SerializedName(value = "datetime")
    DATETIME,
    @SerializedName(value = "file")
    FILE,
    @SerializedName(value = "reference")
    REFERENCE
}
