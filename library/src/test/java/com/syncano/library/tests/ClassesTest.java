package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ClassStatus;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.model.AllTypesObject;
import com.syncano.library.model.Author;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ClassesTest extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testClasses() throws InterruptedException {
        String className = SyncanoClassHelper.getSyncanoClassName(TestSyncanoObject.class);
        Response<SyncanoClass> respDelete = syncano.deleteSyncanoClass(className).send();
        assertTrue(respDelete.isSuccess());

        SyncanoClass syncanoClass = new SyncanoClass(TestSyncanoObject.class);
        syncanoClass.setDescription("First description");

        // ----------------- Create -----------------

        Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
        assertTrue(responseCreateClass.isSuccess());
        assertNotNull(responseCreateClass.getData());
        syncanoClass = responseCreateClass.getData();

        // wait until class will finish to create on server
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < (10 * 60 * 1000) && (syncanoClass.getStatus() != ClassStatus.READY)) {
            Thread.sleep(100);
            SyncanoLog.d(SyncanoApplicationTestCase.class.getSimpleName(), "Waiting for class to create: " + (System.currentTimeMillis() - start));
            Response<SyncanoClass> respClass = syncano.getSyncanoClass(syncanoClass.getName()).send();
            assertEquals(Response.HTTP_CODE_SUCCESS, respClass.getHttpResultCode());
            syncanoClass = respClass.getData();
            if (syncanoClass != null && syncanoClass.getStatus() == ClassStatus.READY) {
                break;
            }
        }

        // ----------------- Get One -----------------
        Response<SyncanoClass> responseGetClass = syncano.getSyncanoClass(syncanoClass.getName()).send();
        assertTrue(responseGetClass.isSuccess());
        assertNotNull(responseGetClass.getData());
        assertEquals(className, responseGetClass.getData().getName());
        assertEquals(SyncanoClassHelper.getSyncanoClassSchema(TestSyncanoObject.class), responseGetClass.getData().getSchema());

        // ----------------- Update -----------------
        String description = "Second description";
        syncanoClass.setDescription(description);
        Response<SyncanoClass> responseUpdateClass = syncano.updateSyncanoClass(syncanoClass).send();
        assertTrue(responseUpdateClass.isSuccess());
        assertNotNull(responseUpdateClass.getData());
        assertEquals(responseUpdateClass.getData().getDescription(), description);


        // ----------------- Get List -----------------
        Response<List<SyncanoClass>> responseGetClasses = syncano.getSyncanoClasses().send();

        assertNotNull(responseGetClasses.getData());
        assertTrue("List should contain at least one item.", responseGetClasses.getData().size() > 0);

        // ----------------- Delete -----------------
        Response responseDeleteClass = syncano.deleteSyncanoClass(className).send();

        assertTrue(responseDeleteClass.isSuccess());
    }

    @Test
    public void testDataTypes() throws InterruptedException {
        createClass(Author.class);
        createClass(AllTypesObject.class);

        // full object with references etc
        AllTypesObject obj1 = AllTypesObject.generateObject(new AllTypesObject());
        AllTypesObject obj2 = AllTypesObject.generateObject(new AllTypesObject());
        assertTrue(obj2.save().isSuccess());
        assertTrue(obj1.someReference.save().isSuccess());
        obj1.reference = obj2;
        Response<AllTypesObject> resp = syncano.createObject(obj1, false).send();
        assertTrue(resp.isSuccess());
        obj1.checkEquals(resp.getData(), false);

        // empty fields object
        AllTypesObject objEmpty = new AllTypesObject();
        resp = syncano.createObject(objEmpty, false).send();
        assertTrue(resp.isSuccess());
        objEmpty.checkEquals(objEmpty, false);
    }


}