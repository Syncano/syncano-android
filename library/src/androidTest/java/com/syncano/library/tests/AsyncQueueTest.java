package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.Config;
import com.syncano.library.Syncano;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.CodeBox;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class AsyncQueueTest extends ApplicationTestCase<Application> {

    private static final String TAG = AsyncQueueTest.class.getSimpleName();
    private static final int TIMEOUT_MILLIS = 20 * 1000;
    private static final int TEST_REQUESTS = 10;

    private CountDownLatch lock;
    private Syncano syncano;

    public AsyncQueueTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAsyncTest() throws InterruptedException {


        lock = new CountDownLatch(TEST_REQUESTS);

        SyncanoCallback<List<CodeBox>> callback = new SyncanoCallback<List<CodeBox>>() {
            @Override
            public void success(Response<List<CodeBox>> response, List<CodeBox> result) {
                assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
                lock.countDown();
            }

            @Override
            public void failure(Response<List<CodeBox>> response) {
                fail(response.getHttpReasonPhrase());
            }
        };

        for (int i = 0; i < TEST_REQUESTS; i++) {
            RequestGetList<CodeBox> requestGetList = syncano.getCodeBoxes();
            requestGetList.setLimit(1);
            requestGetList.sendAsync(callback);
        }

        lock.await(TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        assertEquals("Not all threads finished work.", 0, lock.getCount());
    }
}