package com.syncano.android.lib.api.account;

import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.AdminFull;

import java.lang.reflect.Type;

public class AccountGetOne extends BaseApiMethod <AdminFull> {

    public AccountGetOne() {
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

        return AdminFull.class;
    }
}
