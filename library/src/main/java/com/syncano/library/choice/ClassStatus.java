package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum ClassStatus {
    @SerializedName(value = "ready")
    READY,

    @SerializedName(value = "migrating")
    MIGRATING
}
