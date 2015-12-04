package com.syncano.library;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;


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
        Syncano.init(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY, BuildConfig.INSTANCE_NAME);
        syncano = Syncano.getInstance();
    }

    public void createClass(Class<? extends SyncanoObject> clazz) {
        removeClass(clazz);
        Response resp = syncano.createSyncanoClass(clazz).send();
        assertEquals(Response.HTTP_CODE_CREATED, resp.getHttpResultCode());
    }

    public void removeClass(Class<? extends SyncanoObject> clazz) {
        syncano.deleteSyncanoClass(clazz).send();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}