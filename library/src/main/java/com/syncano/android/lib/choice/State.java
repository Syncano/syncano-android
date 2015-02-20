package com.syncano.android.lib.choice;

import com.google.gson.annotations.SerializedName;

public enum State {
    @SerializedName(value = "new")
    NEW,

    @SerializedName(value = "declined")
    DECLINED,

    @SerializedName(value = "accepted")
    ACCEPTED
}
