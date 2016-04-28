package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.Script;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AsyncQueueTest extends SyncanoApplicationTestCase {

    private static final int TIMEOUT_MILLIS = 20 * 1000;
    private static final int TEST_REQUESTS = 10;

    private CountDownLatch lock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testAsyncTest() throws InterruptedException {
        lock = new CountDownLatch(TEST_REQUESTS);

        SyncanoListCallback<Script> callback = new SyncanoListCallback<Script>() {
            @Override
            public void success(ResponseGetList<Script> response, List<Script> result) {
                assertTrue(response.isSuccess());
                lock.countDown();
            }

            @Override
            public void failure(ResponseGetList<Script> response) {
                fail(response.getError() + " " + response.getHttpReasonPhrase());
                lock.countDown();
            }
        };

        for (int i = 0; i < TEST_REQUESTS; i++) {
            RequestGetList<Script> requestGetList = syncano.getScripts();
            requestGetList.setLimit(1);
            requestGetList.sendAsync(callback);
        }

        lock.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        assertEquals("Not all threads finished work.", 0, lock.getCount());
    }
}