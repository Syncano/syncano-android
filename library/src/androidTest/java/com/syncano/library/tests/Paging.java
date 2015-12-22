package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Paging extends SyncanoApplicationTestCase {

    public static final int NUMBER = 10;
    public static final int PAGE = 4;
    public static final int LOOPS = NUMBER / PAGE + ((NUMBER % PAGE > 0) ? 1 : 0);

    private CountDownLatch countDownLatch;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(MyObject.class);

        Random rnd = new Random();
        for (int i = 0; i < NUMBER; i++) {
            assertTrue((new MyObject(rnd.nextInt())).save().isSuccess());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        removeClass(MyObject.class);
        super.tearDown();
    }

    public void testPaging() {
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

        assertEquals(list.size(), NUMBER);
        assertEquals(LOOPS, loops);
    }

    public void testPleasePaging() {
        ArrayList<MyObject> list = new ArrayList<>();
        int loops = 0;

        ResponseGetList<MyObject> resp = Syncano.please(MyObject.class).limit(PAGE).get();
        loops++;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.hasNextPage()) {
            resp = Syncano.please(MyObject.class).page(resp.getNextPageUrl()).get();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(list.size(), NUMBER);
        assertEquals(LOOPS, loops);
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
                assertEquals(list.size(), NUMBER);
                assertEquals(LOOPS, loops);
                countDownLatch.countDown();
            }
        }

        @Override
        public void failure(ResponseGetList<MyObject> response) {
            fail();
        }
    };

    public void testAsyncPaging() throws InterruptedException {
        Syncano.please(MyObject.class).limit(PAGE).get(callbackPlease);
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    public void testOrderPaging() {
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

        assertEquals(list.size(), NUMBER);
        assertEquals(LOOPS, loops);
    }

    @SyncanoClass(name = "myobject")
    private static class MyObject extends SyncanoObject {
        public static final String FIELD_NUMBER = "some_number";

        MyObject(int number) {
            this.number = number;
        }

        @SyncanoField(name = FIELD_NUMBER, orderIndex = true)
        int number;
    }
}
