package com.syncano.android.lib.api.account;

import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.AdminFull;

import java.lang.reflect.Type;

public class AccountUpdate extends BaseApiMethod <AdminFull> {

    @SyncanoParam(required = true)
    private String email;

    @SyncanoParam(name = "first_name")
    private String firstName;

    @SyncanoParam(name = "last_name")
    private String lastName;

    public AccountUpdate(String email) {
        this.email = email;
    }

    @Override
    protected String getRequestMethod() {
        return METHOD_PUT;
    }

    @Override
    protected String getMethod() {
        return "/v1/account/";
    }

    @Override
    protected Type getResultType() {
        return AdminFull.class;
    }
}
