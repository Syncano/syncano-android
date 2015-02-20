package com.syncano.android.lib.api.account;

import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.Account;

import java.lang.reflect.Type;

public class AccountRegister extends BaseApiMethod <Account> {

    @SyncanoParam(required = true)
    private String email;

    @SyncanoParam(required = true)
    private String password;

    @SyncanoParam(name = "first_name")
    private String firstName;

    @SyncanoParam(name = "last_name")
    private String lastName;

    public AccountRegister(String email, String password) {
        this.email = email;
        this.password = password;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
