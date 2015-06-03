package com.syncano.library.choice;

import com.google.gson.annotations.SerializedName;
import com.syncano.library.Constants;

public enum SocialAuthBackend {

    @SerializedName(value = Constants.SOCIAL_AUTH_FACEBOOK)
    FACEBOOK(Constants.SOCIAL_AUTH_FACEBOOK),

    @SerializedName(value = Constants.SOCIAL_AUTH_GOOGLE_OAUTH2)
    GOOGLE_OAUTH2(Constants.SOCIAL_AUTH_GOOGLE_OAUTH2);

    private final String text;

    SocialAuthBackend(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
