package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum State {
    @SerializedName(value = "new")
    NEW,

    @SerializedName(value = "declined")
    DECLINED,

    @SerializedName(value = "accepted")
    ACCEPTED
}
