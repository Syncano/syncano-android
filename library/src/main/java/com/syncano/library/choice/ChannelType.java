package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum ChannelType {

    @SerializedName(value = "default")
    DEFAULT,

    @SerializedName(value = "separate_rooms")
    SEPARATE_ROOMS,
}
