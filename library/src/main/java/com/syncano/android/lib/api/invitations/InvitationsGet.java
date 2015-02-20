package com.syncano.android.lib.api.invitations;

import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.api.BaseGetMethod;
import com.syncano.android.lib.data.AdminInvitation;
import com.syncano.android.lib.data.Page;

import java.lang.reflect.Type;

public class InvitationsGet extends BaseGetMethod<Page<AdminInvitation>> {

    @Override
    protected String getMethod() {
        return "/v1/account/invitations/";
    }

    @Override
    protected Type getResultType() {
        return (new TypeToken<Page<AdminInvitation>>() {}).getType();
    }
}
