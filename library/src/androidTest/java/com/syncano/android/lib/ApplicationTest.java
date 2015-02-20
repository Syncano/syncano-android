package com.syncano.android.lib;

import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.api.account.AccountGet;
import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.data.Account;
import com.syncano.android.lib.data.Invitation;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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

        Syncano.init("c39742252034618f71c5d7e9ff556fe21464d0ee");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAccount() {

        Response<Account> response = new AccountGet().send();




    }
}