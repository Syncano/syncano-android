package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum DataObjectPermissions {

    @SerializedName(value = "none")
    NONE,

    @SerializedName(value = "read")
    READ,

    @SerializedName(value = "write")
    WRITE,

    @SerializedName(value = "full")
    FULL,
}
