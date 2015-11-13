package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.utils.SyncanoClassHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DataObjectsTest extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Make sure class exists.
        String className = SyncanoClassHelper.getSyncanoClassName(TestSyncanoObject.class);
        Response<SyncanoClass> responseGetSyncanoClass = syncano.getSyncanoClass(className).send();

        if (responseGetSyncanoClass.getHttpResultCode() == Response.HTTP_CODE_NOT_FOUND) {
            SyncanoClass syncanoClass = new SyncanoClass(TestSyncanoObject.class);
            Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
            assertEquals(responseCreateClass.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateClass.getHttpResultCode());
        }
    }

    public void testDataObjects() throws InterruptedException {

        String valueOne = "user01";
        String valueTwo = "pass01";
        String newValueOne = "user02";

        final TestSyncanoObject newTestObject = new TestSyncanoObject();
        newTestObject.valueOne = valueOne;
        newTestObject.valueTwo = valueTwo;

        final TestSyncanoObject testObject;

        // ----------------- Create -----------------
        Response<TestSyncanoObject> responseCreateObject = syncano.createObject(newTestObject).send();

        assertEquals(responseCreateObject.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        assertNotNull(responseCreateObject.getData());
        testObject = responseCreateObject.getData();

        // ----------------- Get One -----------------
        Response<TestSyncanoObject> responseGetUser = syncano.getObject(TestSyncanoObject.class, testObject.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetUser.getHttpResultCode());
        assertNotNull(responseGetUser.getData());
        assertEquals(testObject.valueOne, responseGetUser.getData().valueOne);
        assertEquals(testObject.valueOne, responseGetUser.getData().valueOne);

        // ----------------- Update -----------------
        testObject.valueOne = newValueOne;
        Response<TestSyncanoObject> responseUpdateUser = syncano.updateObject(testObject).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateUser.getHttpResultCode());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(testObject.valueOne, responseUpdateUser.getData().valueOne);
        assertEquals(testObject.valueOne, responseUpdateUser.getData().valueOne);

        // ----------------- Get List -----------------
        Response<List<TestSyncanoObject>> responseGetCodeBoxes = syncano.getObjects(TestSyncanoObject.class).send();

        assertNotNull(responseGetCodeBoxes.getData());
        assertTrue("List should contain at least one item.", responseGetCodeBoxes.getData().size() > 0);

        // ----------------- Delete -----------------
        Response responseDeleteObject = syncano.deleteObject(TestSyncanoObject.class, testObject.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteObject.getHttpResultCode());
    }

    public void testWhereFilter() {
        TestSyncanoObject objectOne = syncano.createObject(new TestSyncanoObject("User", "One")).send().getData();
        TestSyncanoObject objectTwo = syncano.createObject(new TestSyncanoObject("User", "Two")).send().getData();

        Where where = new Where();
        where.eq(TestSyncanoObject.FIELD_ID, objectOne.getId()).neq(TestSyncanoObject.FIELD_ID, objectTwo.getId());

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setWhereFilter(where);
        Response<List<TestSyncanoObject>> response = requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size()); // Should be only one item with given id.


        syncano.deleteObject(TestSyncanoObject.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoObject.class, objectTwo.getId());
    }

    public void testOrderBy() {
        TestSyncanoObject objectOne = syncano.createObject(new TestSyncanoObject("User", "One")).send().getData();
        TestSyncanoObject objectTwo = syncano.createObject(new TestSyncanoObject("User", "Two")).send().getData();

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setOrderBy(TestSyncanoObject.FIELD_ID, true);
        Response<List<TestSyncanoObject>> response = requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertTrue(response.getData().size() > 1);
        // If order is descending, first id on list should be bigger.
        assertTrue(response.getData().get(0).getId() > response.getData().get(1).getId());

        syncano.deleteObject(TestSyncanoObject.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoObject.class, objectTwo.getId());
    }

    public void testPageSize() {
        TestSyncanoObject objectOne = syncano.createObject(new TestSyncanoObject("User", "One")).send().getData();
        TestSyncanoObject objectTwo = syncano.createObject(new TestSyncanoObject("User", "Two")).send().getData();

        int limitItems = 1;

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setLimit(limitItems);
        Response<List<TestSyncanoObject>> response = requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(limitItems, response.getData().size());

        syncano.deleteObject(TestSyncanoObject.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoObject.class, objectTwo.getId());
    }

    public void testLastPk() {

        int testObjectCount = 3;
        int limitItems = 1;

        List<TestSyncanoObject> objects = new ArrayList<>(testObjectCount);

        // Create test objects
        for (int i = 0; i < testObjectCount; i++) {
            TestSyncanoObject testObject = new TestSyncanoObject("valueOne: " + i, "valueTwo: " + i);
            Response<TestSyncanoObject> responseCreateObject = syncano.createObject(testObject).send();
            assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
            objects.add(responseCreateObject.getData());
        }

        int idInMiddle = objects.get(objects.size() / 2).getId();

        // Get with bigger id
        RequestGetList<TestSyncanoObject> requestNext = syncano.getObjects(TestSyncanoObject.class);
        requestNext.setLimit(limitItems);
        requestNext.setLastPk(idInMiddle, false);
        Response<List<TestSyncanoObject>> responseNext = requestNext.send();

        assertEquals(responseNext.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responseNext.getHttpResultCode());
        assertTrue(responseNext.getData().get(0).getId() > idInMiddle);

        // Get with smaller id
        RequestGetList<TestSyncanoObject> requestPrevious = syncano.getObjects(TestSyncanoObject.class);
        requestPrevious.setLimit(limitItems);
        requestPrevious.setLastPk(idInMiddle, true);
        Response<List<TestSyncanoObject>> responsePrevious = requestPrevious.send();

        assertEquals(responsePrevious.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responsePrevious.getHttpResultCode());
        assertTrue(responsePrevious.getData().get(0).getId() < idInMiddle);

        // Clean after test
        for (TestSyncanoObject objectToDelete : objects) {
            Response responseDeleteObject = syncano.deleteObject(TestSyncanoObject.class, objectToDelete.getId()).send();
            assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteObject.getHttpResultCode());
        }
    }
}