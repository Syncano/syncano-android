package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.choice.RuntimeName;
import com.syncano.android.lib.data.CodeBox;
import com.syncano.android.lib.data.RunCodeBoxResult;
import com.syncano.android.lib.data.Webhook;

import java.util.concurrent.CountDownLatch;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class WebhooksTest extends ApplicationTestCase<Application> {

    private static final String TAG = WebhooksTest.class.getSimpleName();

    private final static int TIMEOUT_MILLIS = 10 * 1000;

    private Syncano syncano;
    private CodeBox codeBox;

    private static final String SLUG = "slug01";
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

        final CodeBox newCodeBox = new CodeBox(codeBoxName, source, runtime);

        // ----------------- Create CodeBox -----------------
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCodeBoxCreate.getHttpResultCode());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();

        // ----------------- Delete Webhook -----------------
        // Make sure slug is not taken.
        syncano.deleteWebhook(SLUG).send();
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isOk());
    }

    public void testWebhooks() throws InterruptedException {

        final Webhook newWebhook = new Webhook(SLUG, 1);
        Webhook webhook;

        // ----------------- Create -----------------
        Response <Webhook> responseCreateWebhooks = syncano.createWebhook(newWebhook).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateWebhooks.getHttpResultCode());
        assertNotNull(responseCreateWebhooks.getData());
        webhook = responseCreateWebhooks.getData();

        // ----------------- Get One -----------------
        Response <Webhook> responseGetWebhook = syncano.getWebhook(SLUG).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetWebhook.getHttpResultCode());
        assertNotNull(responseGetWebhook.getData());
        assertEquals(webhook.getSlug(), responseGetWebhook.getData().getSlug());
        assertEquals(webhook.getCodebox(), responseGetWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseGetWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseGetWebhook.getData().getIsPublic());

        // ----------------- Update -----------------
        webhook.setCodebox(codeBox.getId());
        Response <Webhook> responseUpdateWebhook = syncano.updateWebhook(webhook).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateWebhook.getHttpResultCode());
        assertNotNull(responseUpdateWebhook.getData());
        assertEquals(webhook.getSlug(), responseUpdateWebhook.getData().getSlug());
        assertEquals(webhook.getCodebox(), responseUpdateWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseUpdateWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseUpdateWebhook.getData().getIsPublic());

        // ----------------- Get Page -----------------
        /*Response <Page<Webhook>> responseGetWebhooks = syncano.getWebhooks().send();

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
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);*/

        // ----------------- Run -----------------
        Response <RunCodeBoxResult> responseRunWebhook = syncano.runWebhook(SLUG).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseRunWebhook.getHttpResultCode());
        assertNotNull(responseRunWebhook.getData());
        assertEquals(EXPECTED_RESULT, responseRunWebhook.getData().getResult());

        // ----------------- Delete -----------------
        Response <Webhook> responseDeleteWebhook = syncano.deleteWebhook(SLUG).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteWebhook.getHttpResultCode());

        // ----------------- Get One -----------------
        Response <Webhook> responseGetOneWebhook = syncano.getWebhook(SLUG).send();

        // After delete, Webhook should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneWebhook.getHttpResultCode());
    }
}