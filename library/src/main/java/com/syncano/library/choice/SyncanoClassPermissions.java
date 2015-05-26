package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum SyncanoClassPermissions {

    @SerializedName(value = "none")
    NONE,

    @SerializedName(value = "read")
    READ,

    @SerializedName(value = "create_objects")
    CREATE_OBJECTS,
}
