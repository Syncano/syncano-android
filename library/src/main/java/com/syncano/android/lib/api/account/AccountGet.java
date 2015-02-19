package com.syncano.android.lib.api.account;

import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.Account;

import java.lang.reflect.Type;

public class AccountGet extends BaseApiMethod <Account> {

    public AccountGet() {
    }

    @Override
    protected String getRequestMethod() {
        return METHOD_GET;
    }

    @Override
    protected String getMethod() {
        return "/v1/account/";
    }

    @Override
    protected Type getResultType() {

        return Account.class;
    }
}
