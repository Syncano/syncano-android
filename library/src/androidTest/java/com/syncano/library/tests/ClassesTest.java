package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.Config;
import com.syncano.library.Syncano;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.RunCodeBoxResult;
import com.syncano.library.data.SyncanoClass;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ClassesTest extends ApplicationTestCase<Application> {

    private static final String TAG = ClassesTest.class.getSimpleName();

    private Syncano syncano;

    public ClassesTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testClasses() throws InterruptedException {

        String className = Syncano.getSyncanoClassName(TestSyncanoClass.class);

        Response<SyncanoClass> responseGetSyncanoClass = syncano.getSyncanoClass(className).send();

        if (responseGetSyncanoClass.isOk()) {
            syncano.deleteSyncanoClass(className).send();
        }

        SyncanoClass syncanoClass = new SyncanoClass(className, TestSyncanoClass.getSchema());

        // ----------------- Create -----------------

        Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
        assertEquals(responseCreateClass.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateClass.getHttpResultCode());
        assertNotNull(responseCreateClass.getData());
        syncanoClass = responseCreateClass.getData();

        // ----------------- Get One -----------------
        Response <SyncanoClass> responseGetClass = syncano.getSyncanoClass(syncanoClass.getName()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetClass.getHttpResultCode());
        assertNotNull(responseGetClass.getData());
        assertEquals(className, responseGetClass.getData().getName());
        assertEquals(TestSyncanoClass.getSchema(), responseGetClass.getData().getSchema());


        // ----------------- Get List -----------------
        Response <List<SyncanoClass>> responseGetClasses = syncano.getSyncanoClasses().send();

        assertNotNull(responseGetClasses.getData());
        assertTrue("List should contain at least one item.", responseGetClasses.getData().size() > 0);

        // ----------------- Delete -----------------
        Response <CodeBox> responseDeleteClass = syncano.deleteSyncanoClass(className).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteClass.getHttpResultCode());
    }
}