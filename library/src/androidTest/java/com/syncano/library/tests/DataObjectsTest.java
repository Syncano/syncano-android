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

        final TestSyncanoClass newUserObject = new TestSyncanoClass();
        newUserObject.valueOne = valueOne;
        newUserObject.valueTwo = valueTwo;

        final TestSyncanoClass testObject;

        // ----------------- Create -----------------
        Response <TestSyncanoClass> responseCreateObject = syncano.createObject(newUserObject).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateObject.getHttpResultCode());
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
        where.eq(TestSyncanoClass.FIELD_ID, objectOne.getId());

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
}