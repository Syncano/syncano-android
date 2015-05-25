package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum Permissions {
    @SerializedName(value = "full")
    FULL,

    @SerializedName(value = "write")
    WRITE,

    @SerializedName(value = "read")
    READ,

    @SerializedName(value = "none")
    NONE
}
