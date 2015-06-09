package com.syncano.library;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.CodeBox;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SyncanoApplicationTestCase extends ApplicationTestCase<Application> {

    protected Syncano syncano;

    public SyncanoApplicationTestCase() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.STAGING_SERVER_URL, Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}