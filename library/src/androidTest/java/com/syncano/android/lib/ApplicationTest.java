package com.syncano.android.lib;

import com.syncano.android.lib.api.account.AccountGet;
import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.data.Account;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    Syncano syncano;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano("");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAccount() {

        Response<Account> response = new AccountGet().send(syncano);
        Account account = response.getData();

        Log.d("test", account.getFirstName());
        Log.d("test", account.getLastName());
        Log.d("test", account.getId());
        Log.d("test", account.getEmail());


        //Response<Invitation>
    }
}