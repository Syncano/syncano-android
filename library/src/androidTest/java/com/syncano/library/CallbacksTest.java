package com.syncano.library;

import android.os.Looper;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CallbacksTest extends SyncanoApplicationTestCase {
    public void setUp() throws Exception {
        super.setUp();
        createClass(Something.class);
    }


    public void tearDown() throws Exception {
        removeClass(Something.class);
        super.tearDown();
    }

    public void testCallbackUiThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        PlatformType.AndroidPlatform platform = (PlatformType.AndroidPlatform) PlatformType.get();
        platform.runOnCallbackThread(new Runnable() {
            @Override
            public void run() {
                if (!isUIThread()) {
                    fail(); // not ui thread
                }

                Syncano.please(Something.class).get(new SyncanoListCallback<Something>() {
                    @Override
                    public void success(ResponseGetList<Something> response, List<Something> result) {
                        if (!isUIThread()) {
                            fail(); // not ui thread
                        }
                        latch.countDown();
                    }

                    @Override
                    public void failure(ResponseGetList<Something> response) {
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

    @com.syncano.library.annotation.SyncanoClass(name = "just_something")
    private static class Something extends SyncanoObject {
        @SyncanoField(name = "some_text")
        public String someText;
    }
}
