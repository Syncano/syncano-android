package com.syncano.android.lib.choice;

import com.google.gson.annotations.SerializedName;

public enum RuntimeName {

    @SerializedName(value = "golang")
    GOLANG,

    @SerializedName(value = "nodejs")
    NODEJS,

    @SerializedName(value = "python")
    PYTHON,

    @SerializedName(value = "ruby")
    RUBY
}
