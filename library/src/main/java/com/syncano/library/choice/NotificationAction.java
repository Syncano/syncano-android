package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;

public enum NotificationAction {

    @SerializedName(value = "custom")
    CUSTOM,

    @SerializedName(value = "create")
    CREATE,

    @SerializedName(value = "update")
    UPDATE,

    @SerializedName(value = "delete")
    DELETE,
}
