package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.choice.SortOrder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DataObjectsTest extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(TestSyncanoObject.class);
    }

    @After
    public void tearDown() throws Exception {
        removeClass(TestSyncanoObject.class);
        super.tearDown();
    }

    @Test
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

        assertTrue(responseCreateObject.isSuccess());
        assertNotNull(responseCreateObject.getData());
        testObject = responseCreateObject.getData();

        // ----------------- Get One -----------------
        Response<TestSyncanoObject> responseGetUser = syncano.getObject(TestSyncanoObject.class, testObject.getId()).send();

        assertTrue(responseGetUser.isSuccess());
        assertNotNull(responseGetUser.getData());
        assertEquals(testObject.valueOne, responseGetUser.getData().valueOne);
        assertEquals(testObject.valueOne, responseGetUser.getData().valueOne);

        // ----------------- Update -----------------
        testObject.valueOne = newValueOne;
        Response<TestSyncanoObject> responseUpdateUser = syncano.updateObject(testObject).send();

        assertTrue(responseUpdateUser.isSuccess());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(testObject.valueOne, responseUpdateUser.getData().valueOne);
        assertEquals(testObject.valueOne, responseUpdateUser.getData().valueOne);

        // ----------------- Get List -----------------
        Response<List<TestSyncanoObject>> responseGetCodeBoxes = syncano.getObjects(TestSyncanoObject.class).send();

        assertNotNull(responseGetCodeBoxes.getData());
        assertTrue("List should contain at least one item.", responseGetCodeBoxes.getData().size() > 0);

        // ----------------- Delete -----------------
        Response responseDeleteObject = syncano.deleteObject(TestSyncanoObject.class, testObject.getId()).send();

        assertTrue(responseDeleteObject.isSuccess());
    }

    @Test
    public void testWhereFilter() {
        TestSyncanoObject objectOne = syncano.createObject(new TestSyncanoObject("User", "One")).send().getData();
        TestSyncanoObject objectTwo = syncano.createObject(new TestSyncanoObject("User", "Two")).send().getData();

        Where where = new Where();
        where.eq(TestSyncanoObject.FIELD_ID, objectOne.getId()).neq(TestSyncanoObject.FIELD_ID, objectTwo.getId());

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setWhereFilter(where);
        Response<List<TestSyncanoObject>> response = requestGetList.send();

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size()); // Should be only one item with given id.
    }

    @Test
    public void testOrderBy() {
        TestSyncanoObject objectOne = syncano.createObject(new TestSyncanoObject("User", "One")).send().getData();
        TestSyncanoObject objectTwo = syncano.createObject(new TestSyncanoObject("User", "Two")).send().getData();

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setOrderBy(TestSyncanoObject.FIELD_ID, SortOrder.DESCENDING);
        Response<List<TestSyncanoObject>> response = requestGetList.send();

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertTrue(response.getData().size() > 1);
        // If order is descending, first id on list should be bigger.
        assertTrue(response.getData().get(0).getId() > response.getData().get(1).getId());

        syncano.deleteObject(TestSyncanoObject.class, objectOne.getId());
        syncano.deleteObject(TestSyncanoObject.class, objectTwo.getId());
    }

    @Test
    public void testPageSize() {
        Response<TestSyncanoObject> objectOne = syncano.createObject(new TestSyncanoObject("User", "One")).send();
        assertTrue(objectOne.isSuccess());
        Response<TestSyncanoObject> objectTwo = syncano.createObject(new TestSyncanoObject("User", "Two")).send();
        assertTrue(objectTwo.isSuccess());

        int limitItems = 1;

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setLimit(limitItems);
        Response<List<TestSyncanoObject>> response = requestGetList.send();

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(limitItems, response.getData().size());
    }
}