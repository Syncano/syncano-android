package com.syncano.android.lib.choice;

import com.google.gson.annotations.SerializedName;

public enum Role {
    @SerializedName(value = "full")
    FULL,

    @SerializedName(value = "write")
    WRITE,

    @SerializedName(value = "read")
    READ
}
