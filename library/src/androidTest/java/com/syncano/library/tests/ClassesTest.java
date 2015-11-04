package com.syncano.library.tests;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.SyncanoClass;

import org.json.JSONArray;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ClassesTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testClasses() throws InterruptedException {

        String className = Syncano.getSyncanoClassName(TestSyncanoClass.class);

        Response<SyncanoClass> responseGetSyncanoClass = syncano.getSyncanoClass(className).send();

        if (responseGetSyncanoClass.isOk()) {
            syncano.deleteSyncanoClass(className).send();
        }

        SyncanoClass syncanoClass = new SyncanoClass(className, TestSyncanoClass.getSchema());
        syncanoClass.setDescription("First description");

        // ----------------- Create -----------------

        Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
        assertEquals(responseCreateClass.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateClass.getHttpResultCode());
        assertNotNull(responseCreateClass.getData());
        syncanoClass = responseCreateClass.getData();

        // ----------------- Get One -----------------
        Response<SyncanoClass> responseGetClass = syncano.getSyncanoClass(syncanoClass.getName()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetClass.getHttpResultCode());
        assertNotNull(responseGetClass.getData());
        assertEquals(className, responseGetClass.getData().getName());
        assertEquals(TestSyncanoClass.getSchema(), responseGetClass.getData().getSchema());

        // ----------------- Update -----------------
        String description = "Second description";
        syncanoClass.setDescription(description);
        Response<SyncanoClass> responseUpdateClass = syncano.updateSyncanoClass(syncanoClass).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateClass.getHttpResultCode());
        assertNotNull(responseUpdateClass.getData());
        assertEquals(responseUpdateClass.getData().getDescription(), description);


        // ----------------- Get List -----------------
        Response<List<SyncanoClass>> responseGetClasses = syncano.getSyncanoClasses().send();

        assertNotNull(responseGetClasses.getData());
        assertTrue("List should contain at least one item.", responseGetClasses.getData().size() > 0);

        // ----------------- Delete -----------------
        Response responseDeleteClass = syncano.deleteSyncanoClass(className).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteClass.getHttpResultCode());
    }
}