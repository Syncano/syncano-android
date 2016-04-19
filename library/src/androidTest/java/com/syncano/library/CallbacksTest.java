package com.syncano.library;

import android.os.Looper;
import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.Model.SomeV1;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class CallbacksTest extends SyncanoAndroidTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(SomeV1.class);
    }

    @After
    public void tearDown() throws Exception {
        removeClass(SomeV1.class);
        super.tearDown();
    }

    @Test
    public void testCallbackUiThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        PlatformType.AndroidPlatform platform = (PlatformType.AndroidPlatform) PlatformType.get();
        platform.runOnCallbackThread(new Runnable() {
            @Override
            public void run() {
                if (!isUIThread()) {
                    fail(); // not ui thread
                }

                Syncano.please(SomeV1.class).get(new SyncanoListCallback<SomeV1>() {
                    @Override
                    public void success(ResponseGetList<SomeV1> response, List<SomeV1> result) {
                        if (!isUIThread()) {
                            fail(); // not ui thread
                        }
                        latch.countDown();
                    }

                    @Override
                    public void failure(ResponseGetList<SomeV1> response) {
                        fail();
                    }
                });
            }
        });

        latch.await(10, TimeUnit.SECONDS);
    }

    private boolean isUIThread() {
        return (Looper.getMainLooper().getThread() == Thread.currentThread());
    }
}
