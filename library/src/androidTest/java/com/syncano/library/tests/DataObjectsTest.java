package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.Config;
import com.syncano.library.Syncano;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoClass;

import java.util.ArrayList;
import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class DataObjectsTest extends ApplicationTestCase<Application> {

    private static final String TAG = DataObjectsTest.class.getSimpleName();

    private Syncano syncano;

    public DataObjectsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);

        // Make sure class exists.
        String className = Syncano.getSyncanoClassName(TestSyncanoClass.class);
        Response<SyncanoClass> responseGetSyncanoClass = syncano.getSyncanoClass(className).send();

        if (responseGetSyncanoClass.getHttpResultCode() == Response.HTTP_CODE_NOT_FOUND) {
            SyncanoClass syncanoClass = new SyncanoClass(className, TestSyncanoClass.getSchema());
            Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
            assertEquals(responseCreateClass.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateClass.getHttpResultCode());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDataObjects() throws InterruptedException {

        String valueOne = "user01";
        String valueTwo = "pass01";
        String newValueOne = "user02";

        final TestSyncanoClass newTestObject = new TestSyncanoClass();
        newTestObject.valueOne = valueOne;
        newTestObject.valueTwo = valueTwo;

        final TestSyncanoClass testObject;

        // ----------------- Create -----------------
        Response <TestSyncanoClass> responseCreateObject = syncano.createObject(newTestObject).send();

        assertEquals(responseCreateObject.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
        assertNotNull(responseCreateObject.getData());
        testObject = responseCreateObject.getData();

        // ----------------- Get One -----------------
        Response <TestSyncanoClass> responseGetUser = syncano.getObject(TestSyncanoClass.class, testObject.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetUser.getHttpResultCode());
        assertNotNull(responseGetUser.getData());
        assertEquals(testObject.valueOne, responseGetUser.getData().valueOne);
        assertEquals(testObject.valueOne, responseGetUser.getData().valueOne);

        // ----------------- Update -----------------
        testObject.valueOne = newValueOne;
        Response <TestSyncanoClass> responseUpdateUser = syncano.updateObject(testObject).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateUser.getHttpResultCode());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(testObject.valueOne, responseUpdateUser.getData().valueOne);
        assertEquals(testObject.valueOne, responseUpdateUser.getData().valueOne);

        // ----------------- Get List -----------------
        Response <List<TestSyncanoClass>> responseGetCodeBoxes = syncano.getObjects(TestSyncanoClass.class).send();

        assertNotNull(responseGetCodeBoxes.getData());
        assertTrue("List should contain at least one item.", responseGetCodeBoxes.getData().size() > 0);

        // ----------------- Delete -----------------
        Response <TestSyncanoClass> responseDeleteObject = syncano.deleteObject(TestSyncanoClass.class, testObject.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteObject.getHttpResultCode());
    }

    public void testWhereFilter() {
        TestSyncanoClass objectOne = (TestSyncanoClass) syncano.createObject(new TestSyncanoClass("User", "One")).send().getData();
        TestSyncanoClass objectTwo = (TestSyncanoClass) syncano.createObject(new TestSyncanoClass("User", "Two")).send().getData();

        Where where = new Where();
        where.eq(TestSyncanoClass.FIELD_ID, objectOne.getId()).neq(TestSyncanoClass.FIELD_ID, objectTwo.getId());

        RequestGetList<TestSyncanoClass> requestGetList = syncano.getObjects(TestSyncanoClass.class);
        requestGetList.setWhereFilter(where);
        Response<List<TestSyncanoClass>> response= requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size()); // Should be only one item with given id.


        syncano.deleteObject(TestSyncanoClass.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoClass.class, objectTwo.getId());
    }

    public void testOrderBy() {
        TestSyncanoClass objectOne = (TestSyncanoClass) syncano.createObject(new TestSyncanoClass("User", "One")).send().getData();
        TestSyncanoClass objectTwo = (TestSyncanoClass) syncano.createObject(new TestSyncanoClass("User", "Two")).send().getData();

        RequestGetList<TestSyncanoClass> requestGetList = syncano.getObjects(TestSyncanoClass.class);
        requestGetList.setOrderBy(TestSyncanoClass.FIELD_ID, true);
        Response<List<TestSyncanoClass>> response= requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertTrue(response.getData().size() > 1);
        // If order is descending, first id on list should be bigger.
        assertTrue(response.getData().get(0).getId() > response.getData().get(1).getId());

        syncano.deleteObject(TestSyncanoClass.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoClass.class, objectTwo.getId());
    }

    public void testPageSize() {
        TestSyncanoClass objectOne = (TestSyncanoClass) syncano.createObject(new TestSyncanoClass("User", "One")).send().getData();
        TestSyncanoClass objectTwo = (TestSyncanoClass) syncano.createObject(new TestSyncanoClass("User", "Two")).send().getData();

        int limitItems = 1;

        RequestGetList<TestSyncanoClass> requestGetList = syncano.getObjects(TestSyncanoClass.class);
        requestGetList.setLimit(limitItems);
        Response<List<TestSyncanoClass>> response= requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(limitItems, response.getData().size());

        syncano.deleteObject(TestSyncanoClass.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoClass.class, objectTwo.getId());
    }

    public void testLastPk() {

        int testObjectCount = 3;
        int limitItems = 1;

        List<TestSyncanoClass> objects = new ArrayList<>(testObjectCount);

        // Create test objects
        for (int i = 0; i < testObjectCount; i++) {
            TestSyncanoClass testObject = new TestSyncanoClass("valueOne: " + i, "valueTwo: " + i);
            Response <TestSyncanoClass> responseCreateObject = syncano.createObject(testObject).send();
            assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
            objects.add(responseCreateObject.getData());
        }

        int idInMiddle = objects.get(objects.size() / 2).getId();

        // Get with bigger id
        RequestGetList<TestSyncanoClass> requestNext = syncano.getObjects(TestSyncanoClass.class);
        requestNext.setLimit(limitItems);
        requestNext.setLastPk(idInMiddle, false);
        Response<List<TestSyncanoClass>> responseNext = requestNext.send();

        assertEquals(responseNext.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responseNext.getHttpResultCode());
        assertTrue(responseNext.getData().get(0).getId() > idInMiddle);

        // Get with smaller id
        RequestGetList<TestSyncanoClass> requestPrevious = syncano.getObjects(TestSyncanoClass.class);
        requestPrevious.setLimit(limitItems);
        requestPrevious.setLastPk(idInMiddle, true);
        Response<List<TestSyncanoClass>> responsePrevious= requestPrevious.send();

        assertEquals(responsePrevious.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responsePrevious.getHttpResultCode());
        assertTrue(responsePrevious.getData().get(0).getId() < idInMiddle);

        // Clean after test
        for (TestSyncanoClass objectToDelete : objects) {
            Response <TestSyncanoClass> responseDeleteObject = syncano.deleteObject(TestSyncanoClass.class, objectToDelete.getId()).send();
            assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteObject.getHttpResultCode());
        }
    }
}