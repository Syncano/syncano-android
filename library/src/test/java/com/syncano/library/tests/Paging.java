package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class Paging extends SyncanoApplicationTestCase {

    public static final int NUMBER = 30;
    public static final int PAGE = 4;
    public static final int LOOPS = getExpectedLoops(NUMBER, PAGE);

    private CountDownLatch countDownLatch;
    private int specialItemsCount;
    private final static int SPECIAL_NUMBER = 42;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(MyObject.class);

        Random rnd = new Random();
        specialItemsCount = 0;

        for (int i = 0; i < NUMBER; i++) {
            MyObject object;
            if (rnd.nextBoolean()) {
                object = new MyObject(SPECIAL_NUMBER);
            } else {
                object = new MyObject(rnd.nextInt());
            }
            assertTrue((object.save().isSuccess()));
            if (object.number == SPECIAL_NUMBER) {
                specialItemsCount++;
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        removeClass(MyObject.class);
        super.tearDown();
    }

    private static int getExpectedLoops(int number, int pageSize) {
        return number / pageSize + ((number % pageSize > 0) ? 1 : 0);
    }

    @Test
    public void testPaging() throws InterruptedException {
        // done this way, because creating objects takes the most of the time
        testResponsePaging();
        testResponseQueryPaging();
        testAsyncPaging();
        testOrderPaging();
        testGetAll();
        testAsyncGetAll();
        testGetAllQuery();
        testPleaseQueryPaging();
        testPleasePaging();
    }


    private void testResponsePaging() {
        ArrayList<MyObject> list = new ArrayList<>();
        int loops = 0;

        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE).get();
        loops++;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.hasNextPage()) {
            resp = resp.getNextPage();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(NUMBER, list.size());
        assertEquals(LOOPS, loops);
    }

    private void testPleasePaging() {
        ArrayList<MyObject> list = new ArrayList<>();

        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE).get();
        int loops = 1;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.hasNextPage()) {
            resp = Syncano.please(MyObject.class).page(resp.getNextPageUrl()).get();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(NUMBER, list.size());
        assertEquals(LOOPS, loops);
    }

    private void testResponseQueryPaging() {
        ArrayList<MyObject> list = new ArrayList<>();

        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE)
                .where().eq(MyObject.FIELD_NUMBER, SPECIAL_NUMBER).get();
        int loops = 1;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.hasNextPage()) {
            resp = resp.getNextPage();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(specialItemsCount, list.size());
        assertEquals(getExpectedLoops(specialItemsCount, PAGE), loops);
    }

    private void testPleaseQueryPaging() {
        ArrayList<MyObject> list = new ArrayList<>();
        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE)
                .where().eq(MyObject.FIELD_NUMBER, SPECIAL_NUMBER).get();
        int loops = 1;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.hasNextPage()) {
            resp = Syncano.please(MyObject.class).page(resp.getNextPageUrl())
                    .where().eq(MyObject.FIELD_NUMBER, SPECIAL_NUMBER).get();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(specialItemsCount, list.size());
        assertEquals(getExpectedLoops(specialItemsCount, PAGE), loops);
    }

    private SyncanoListCallback<MyObject> callbackPlease = new SyncanoListCallback<MyObject>() {
        ArrayList<MyObject> list = new ArrayList<>();
        int loops = 0;

        @Override
        public void success(ResponseGetList<MyObject> response, List<MyObject> result) {
            list.addAll(result);
            loops++;

            if (response.hasNextPage()) {
                response.getNextPage(callbackPlease);
            } else {
                assertEquals(NUMBER, list.size());
                assertEquals(LOOPS, loops);
                countDownLatch.countDown();
            }
        }

        @Override
        public void failure(ResponseGetList<MyObject> response) {
            fail();
        }
    };

    private void testAsyncPaging() throws InterruptedException {
        Syncano.please(MyObject.class).limit(PAGE).get(callbackPlease);
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    private void testOrderPaging() {
        ArrayList<MyObject> list = new ArrayList<>();
        int loops = 0;

        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE).
                orderBy(MyObject.FIELD_NUMBER).get();
        loops++;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.hasNextPage()) {
            resp = resp.getNextPage();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(NUMBER, list.size());
        assertEquals(LOOPS, loops);
    }

    private void testGetAll() {
        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE).getAll(true).get();
        assertTrue(resp.isSuccess());
        assertNotNull(resp.getData());
        assertEquals(NUMBER, resp.getData().size());
    }

    private void testAsyncGetAll() throws InterruptedException {
        final AtomicBoolean requestFinished = new AtomicBoolean(false);
        Syncano.please(MyObject.class).limit(PAGE).getAll(true).get(new SyncanoListCallback<MyObject>() {
            @Override
            public void success(ResponseGetList<MyObject> resp, List<MyObject> result) {
                assertTrue(resp.isSuccess());
                assertNotNull(resp.getData());
                assertEquals(NUMBER, resp.getData().size());
                requestFinished.set(true);
                countDownLatch.countDown();
            }

            @Override
            public void failure(ResponseGetList<MyObject> response) {
                fail();
            }
        });
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
        assertTrue(requestFinished.get());
    }

    private void testGetAllQuery() {
        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE).getAll(true)
                .where().eq(MyObject.FIELD_NUMBER, SPECIAL_NUMBER).get();
        assertTrue(resp.isSuccess());
        assertNotNull(resp.getData());
        assertEquals(specialItemsCount, resp.getData().size());
    }

    @SyncanoClass(name = "myobject")
    private static class MyObject extends SyncanoObject {
        public static final String FIELD_NUMBER = "some_number";

        MyObject(int number) {
            this.number = number;
        }

        @SyncanoField(name = FIELD_NUMBER, orderIndex = true, filterIndex = true)
        int number;
    }
}
