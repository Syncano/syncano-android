package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum TraceStatus {
    @SerializedName(value = "success")
    SUCCESS,

    @SerializedName(value = "failure")
    FAILURE,

    @SerializedName(value = "timeout")
    TIMEOUT,

    @SerializedName(value = "pending")
    PENDING
}