package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.choice.RuntimeName;
import com.syncano.android.lib.data.CodeBox;
import com.syncano.android.lib.data.Webhook;

import java.util.concurrent.CountDownLatch;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class WebhooksTest extends ApplicationTestCase<Application> {

    private static final String TAG = WebhooksTest.class.getSimpleName();

    private final static int TIMEOUT_MILLIS = 10 * 1000;

    private Syncano syncano;
    private CountDownLatch lock;
    private final WeakReference<CodeBox> codeBoxWeakReference = new WeakReference<>();

    private static final String SLUG = "slug07";
    private static final String EXPECTED_RESULT = "this is message from our Codebox";

    public WebhooksTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);

        String codeBoxName = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        final CodeBox codeBox = new CodeBox(codeBoxName, source, runtime);

        // ----------------- Create CodeBox -----------------
        /*lock = new CountDownLatch(1);
        syncano.createCodeBox(codeBox, new GetCallback<CodeBox>() {

            @Override
            public void success(CodeBox object) {
                assertNotNull(object);
                codeBoxWeakReference.value = object;
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to create object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Delete Webhook -----------------
        // Make sure slug is not taken.
        lock = new CountDownLatch(1);
        syncano.deleteWebhook(SLUG, new DeleteCallback() {
            @Override
            public void success() {
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                lock.countDown();
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);*/
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        /*lock = new CountDownLatch(1);
        syncano.deleteCodeBox(codeBoxWeakReference.value.getId(), new DeleteCallback() {
            @Override
            public void success() {
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to delete object. " + error.getHttpResultCode());
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);
        super.tearDown();*/
    }

    public void testWebhooks() throws InterruptedException {

        final Webhook webhook = new Webhook(SLUG, 1);

        Response <Webhook> responseCreateWebhooks = syncano.createWebhook(webhook).send();
        Log.d("Webhook Test", "Created:" + responseCreateWebhooks.getData());
        Response <Webhook> responseGetWebhooks = syncano.getWebhooks().send();

        /*final WeakReference<Webhook> resultRef = new WeakReference<>();

        // ----------------- Create -----------------
        lock = new CountDownLatch(1);
        syncano.createWebhook(webhook, new GetCallback<Webhook>() {

            @Override
            public void success(Webhook object) {
                assertNotNull(object);
                resultRef.value = object;
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to create object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get One -----------------
        lock = new CountDownLatch(1);
        syncano.getWebhook(resultRef.value.getSlug(), new GetCallback<Webhook>() {
            @Override
            public void success(Webhook object) {
                assertNotNull(object);
                assertEquals(resultRef.value.getSlug(), object.getSlug());
                assertEquals(resultRef.value.getCodebox(), object.getCodebox());
                assertEquals(resultRef.value.getPublicLink(), object.getPublicLink());
                assertEquals(resultRef.value.getIsPublic(), object.getIsPublic());
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to get object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Update -----------------
        resultRef.value.setCodebox(codeBoxWeakReference.value.getId());
        lock = new CountDownLatch(1);
        syncano.updateWebhook(resultRef.value, new GetCallback<Webhook>() {
            @Override
            public void success(Webhook object) {
                assertNotNull(object);
                assertEquals(resultRef.value.getSlug(), object.getSlug());
                assertEquals(resultRef.value.getCodebox(), object.getCodebox());
                assertEquals(resultRef.value.getPublicLink(), object.getPublicLink());
                assertEquals(resultRef.value.getIsPublic(), object.getIsPublic());
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to update object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get Page -----------------
        lock = new CountDownLatch(1);
        syncano.getWebhooks(new GetCallback<Page<Webhook>>() {
            @Override
            public void success(Page<Webhook> page) {
                assertNotNull(page);
                assertNotNull(page.getObjects());
                assertTrue("List should contain at least one item.", page.getObjects().size() > 0);
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to get list");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Run -----------------
        lock = new CountDownLatch(1);
        syncano.runWebhook(resultRef.value.getSlug(), new GetCallback<RunCodeBoxResult>() {
            @Override
            public void success(RunCodeBoxResult object) {
                assertNotNull(object);
                assertEquals(EXPECTED_RESULT, object.getResult());
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to run CodeBox.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Delete -----------------
        lock = new CountDownLatch(1);
        syncano.deleteWebhook(resultRef.value.getSlug(), new DeleteCallback() {
            @Override
            public void success() {
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to delete object. " + error.getHttpResultCode());
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get One -----------------
        lock = new CountDownLatch(1);
        syncano.getWebhook(resultRef.value.getSlug(), new GetCallback<Webhook>() {
            @Override
            public void success(Webhook object) {
                assertNull("Failed to delete.", object);
            }

            @Override
            public void failure(SyncanoException error) {
                lock.countDown();
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);*/
    }
}