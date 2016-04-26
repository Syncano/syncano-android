package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.api.Where;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.model.StringGenerator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testWhereFilter() throws InterruptedException {
        final String val = "a";
        final int itemsNum = 2;
        new TestSyncanoObject(val, "").save();
        new TestSyncanoObject(val, "").save();
        new TestSyncanoObject("b", "").save();
        new TestSyncanoObject("c", "").save();

        // get item with same id, call on syncano object
        Where where = new Where();
        where.eq(TestSyncanoObject.FIELD_VAL_ONE, val);

        RequestGetList<TestSyncanoObject> requestGetList = syncano.getObjects(TestSyncanoObject.class);
        requestGetList.setWhereFilter(where);
        ResponseGetList<TestSyncanoObject> response = requestGetList.send();

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(itemsNum, response.getData().size()); // Should be only one item with given id.

        // check all wrapped methods
        response = Syncano.please(TestSyncanoObject.class).where().eq(TestSyncanoObject.FIELD_VAL_ONE, val).get();
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(itemsNum, response.getData().size());

        response = Syncano.please(TestSyncanoObject.class).where().eq(TestSyncanoObject.FIELD_VAL_ONE, val).getAll();
        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertEquals(itemsNum, response.getData().size());

        // check asynchronous wrapped methods
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Syncano.please(TestSyncanoObject.class).where().eq(TestSyncanoObject.FIELD_VAL_ONE, val).get(new SyncanoListCallback<TestSyncanoObject>() {
            @Override
            public void success(ResponseGetList<TestSyncanoObject> response, List<TestSyncanoObject> result) {
                assertTrue(response.isSuccess());
                assertNotNull(response.getData());
                assertEquals(itemsNum, response.getData().size());
                countDownLatch.countDown();
            }

            @Override
            public void failure(ResponseGetList<TestSyncanoObject> response) {
                fail();
            }
        });

        Syncano.please(TestSyncanoObject.class).where().eq(TestSyncanoObject.FIELD_VAL_ONE, val).getAll(new SyncanoListCallback<TestSyncanoObject>() {
            @Override
            public void success(ResponseGetList<TestSyncanoObject> response, List<TestSyncanoObject> result) {
                assertTrue(response.isSuccess());
                assertNotNull(response.getData());
                assertEquals(itemsNum, response.getData().size());
                countDownLatch.countDown();
            }

            @Override
            public void failure(ResponseGetList<TestSyncanoObject> response) {
                fail();
            }
        });

        countDownLatch.await(5, TimeUnit.SECONDS);
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

    @Test
    public void testPolishEncoding() {
        String s1 = "ą, ć, ę, ł, ń, ó, ś, ź, ż.";
        String s2 = "";
        TestSyncanoObject o = new TestSyncanoObject(s1, s2);
        Response<TestSyncanoObject> resp = o.save();
        assertTrue(resp.isSuccess());
        assertEquals(o.valueOne, s1);
        assertEquals(o.valueTwo, s2);
    }

    @Test
    public void testUpdate() {
        TestSyncanoObject o = new TestSyncanoObject("a", "b");
        assertTrue(o.save().isSuccess());

        o.valueOne = "c";
        Response<TestSyncanoObject> resp = syncano.updateObject(o, false).send();
        assertTrue(resp.isSuccess());
        assertTrue(o != resp.getData());
        assertTrue(o.getUpdatedAt().before(resp.getData().getUpdatedAt()));

        resp = syncano.updateObject(o, true).send();
        assertTrue(resp.isSuccess());
        assertTrue(o == resp.getData());
        assertTrue(o.getUpdatedAt().equals(resp.getData().getUpdatedAt()));
    }
}