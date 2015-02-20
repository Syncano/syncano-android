package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.api.account.AccountGetOne;
import com.syncano.android.lib.data.AdminFull;

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

        Response<AdminFull> response = new AccountGetOne().send();




    }
}