package com.syncano.library;

import android.app.Application;
import android.test.ApplicationTestCase;


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
        Syncano.init(Config.STAGING_SERVER_URL, Config.API_KEY, Config.INSTANCE_NAME);
        syncano = Syncano.getInstance();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}