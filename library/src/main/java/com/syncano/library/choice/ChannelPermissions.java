package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum ChannelPermissions {

    @SerializedName(value = "none")
    NONE,

    @SerializedName(value = "subscribe")
    SUBSCRIBE,

    @SerializedName(value = "publish")
    PUBLISH,
}
