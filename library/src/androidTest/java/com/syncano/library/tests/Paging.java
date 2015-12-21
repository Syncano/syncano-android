package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class Paging extends SyncanoApplicationTestCase {

    public static final int NUMBER = 10;
    public static final int PAGE = 4;
    public static final int LOOPS = NUMBER / PAGE + ((NUMBER % PAGE > 0) ? 1 : 0);

    private CountDownLatch countDownLatch;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(TestSyncanoObject.class);

        for (int i = 0; i < NUMBER; i++) {
            assertTrue((new TestSyncanoObject()).save().isSuccess());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        removeClass(TestSyncanoObject.class);
        super.tearDown();
    }

    public void testPaging() {
        ArrayList<TestSyncanoObject> list = new ArrayList<>();
        int loops = 0;

        RequestGetList<TestSyncanoObject> req = syncano.getObjects(TestSyncanoObject.class);
        req.setLimit(PAGE);
        ResponseGetList<TestSyncanoObject> resp = req.send();
        loops++;
        assertTrue(resp.isSuccess());
        assertEquals(PAGE, resp.getData().size());
        list.addAll(resp.getData());

        while (resp.getLinkNext() != null) {
            resp = (syncano.getObjects(TestSyncanoObject.class, resp.getLinkNext())).send();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(list.size(), NUMBER);
        assertEquals(LOOPS, loops);
    }

    public void testPleasePaging() {
        ArrayList<TestSyncanoObject> list = new ArrayList<>();
        int loops = 0;

        ResponseGetList<TestSyncanoObject> resp = SyncanoObject.please(TestSyncanoObject.class).limit(PAGE).get();
        loops++;
        assertTrue(resp.isSuccess());
        list.addAll(resp.getData());

        while (resp.getLinkNext() != null) {
            resp = SyncanoObject.please(TestSyncanoObject.class).page(resp.getLinkNext()).get();
            loops++;
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }

        assertEquals(list.size(), NUMBER);
        assertEquals(LOOPS, loops);
    }

    private SyncanoListCallback<TestSyncanoObject> callback = new SyncanoListCallback<TestSyncanoObject>() {
        ArrayList<TestSyncanoObject> list = new ArrayList<>();
        int loops = 0;

        @Override
        public void success(ResponseGetList<TestSyncanoObject> response, List<TestSyncanoObject> result) {
            list.addAll(result);
            loops++;

            if (response.getLinkNext() != null) {
                (syncano.getObjects(TestSyncanoObject.class, response.getLinkNext())).sendAsync(callback);
            } else {
                assertEquals(list.size(), NUMBER);
                assertEquals(LOOPS, loops);
                countDownLatch.countDown();
            }
        }

        @Override
        public void failure(ResponseGetList<TestSyncanoObject> response) {
            fail();
        }
    };

    public void testAsyncPaging() throws InterruptedException {
        RequestGetList<TestSyncanoObject> req = syncano.getObjects(TestSyncanoObject.class);
        req.setLimit(PAGE);
        req.sendAsync(callback);
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    private SyncanoListCallback<TestSyncanoObject> callbackPlease = new SyncanoListCallback<TestSyncanoObject>() {
        ArrayList<TestSyncanoObject> list = new ArrayList<>();
        int loops = 0;

        @Override
        public void success(ResponseGetList<TestSyncanoObject> response, List<TestSyncanoObject> result) {
            list.addAll(result);
            loops++;

            if (response.getLinkNext() != null) {
                SyncanoObject.please(TestSyncanoObject.class).page(response.getLinkNext()).getAsync(callbackPlease);
            } else {
                assertEquals(list.size(), NUMBER);
                assertEquals(LOOPS, loops);
                countDownLatch.countDown();
            }
        }

        @Override
        public void failure(ResponseGetList<TestSyncanoObject> response) {
            fail();
        }
    };

    public void testPleaseAsyncPaging() throws InterruptedException {
        SyncanoObject.please(TestSyncanoObject.class).limit(PAGE).getAsync(callbackPlease);
        countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
