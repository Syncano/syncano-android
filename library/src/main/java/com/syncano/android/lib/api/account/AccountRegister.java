package com.syncano.android.lib.api.account;

import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.Account;

import java.lang.reflect.Type;

public class AccountRegister extends BaseApiMethod <Account> {

    private String email;


    private String password;

    // first_name
    private String firstName;

    // last_name
    private String lastName;

    @Override
    protected String getRequestMethod() {
        return METHOD_POST;
    }

    @Override
    protected String getMethod() {
        return "/v1/account/register/";
    }

    @Override
    protected Type getResultType() {
        return Account.class;
    }
}
