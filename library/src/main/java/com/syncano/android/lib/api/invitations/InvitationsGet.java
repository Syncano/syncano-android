package com.syncano.android.lib.api.invitations;

import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.Invitation;
import com.syncano.android.lib.data.Page;

import java.lang.reflect.Type;

public class InvitationsGet extends BaseApiMethod <Page<Invitation>> {

    @Override
    protected String getRequestMethod() {
        return null;
    }

    @Override
    protected String getMethod() {
        return null;
    }

    @Override
    protected Type getResultType() {
        return (new TypeToken<Page<Invitation>>() {}).getType();
    }
}
