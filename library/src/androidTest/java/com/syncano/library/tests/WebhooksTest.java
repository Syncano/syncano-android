package com.syncano.library.tests;

import com.syncano.library.BuildConfig;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.Webhook;

import java.util.List;

public class WebhooksTest extends SyncanoApplicationTestCase {

    private CodeBox codeBox;

    private static final String WEBHOOK_NAME = "webhook_test";
    private static final String PUBLIC_WEBHOOK_NAME = "public_webhook";
    private static final String EXPECTED_RESULT = "This is message from our Codebox";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String codeBoxLabel = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";
        CodeBox newCodeBox = new CodeBox(codeBoxLabel, source, runtime);

        // ----------------- Create CodeBox -----------------
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();

        assertTrue(responseCodeBoxCreate.isSuccess());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();

        // ----------------- Delete Webhook -----------------
        Response<Webhook> delResp = syncano.deleteWebhook(WEBHOOK_NAME).send();
        assertTrue(delResp.isSuccess());

        // ----------------- Create Webhook -----------------
        Webhook newWebhook = new Webhook(WEBHOOK_NAME, codeBox.getId());
        Response<Webhook> responseCreateWebhook = syncano.createWebhook(newWebhook).send();

        assertTrue(responseCreateWebhook.isSuccess());
        assertNotNull(responseCreateWebhook.getData());
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());

        // ----------------- Delete Webhook -----------------
        Response<Webhook> delResp = syncano.deleteWebhook(WEBHOOK_NAME).send();
        assertTrue(delResp.isSuccess());
    }

    public void testWebhooks() throws InterruptedException {
        // ----------------- Get One -----------------
        Response<Webhook> responseGetWebhook = syncano.getWebhook(WEBHOOK_NAME).send();

        assertTrue(responseGetWebhook.isSuccess());
        assertNotNull(responseGetWebhook.getData());
        Webhook webhook = responseGetWebhook.getData();
        assertEquals(webhook.getName(), responseGetWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseGetWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseGetWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseGetWebhook.getData().getIsPublic());

        // ----------------- Update -----------------
        webhook.setCodebox(codeBox.getId());
        Response<Webhook> responseUpdateWebhook = syncano.updateWebhook(webhook).send();

        assertTrue(responseUpdateWebhook.isSuccess());
        assertNotNull(responseUpdateWebhook.getData());
        assertEquals(webhook.getName(), responseUpdateWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseUpdateWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseUpdateWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseUpdateWebhook.getData().getIsPublic());

        // ----------------- Get List -----------------
        Response<List<Webhook>> responseGetWebhooks = syncano.getWebhooks().send();

        assertNotNull(responseGetWebhooks.getData());
        assertTrue("List should contain at least one item.", responseGetWebhooks.getData().size() > 0);

        // ----------------- Run without key-----------------
        Syncano noKeySyncano = new Syncano(BuildConfig.INSTANCE_NAME);
        Response<Trace> responseRunNoKey = noKeySyncano.runWebhook(WEBHOOK_NAME).send();

        assertEquals(responseRunNoKey.getHttpReasonPhrase(), Response.HTTP_CODE_FORBIDDEN, responseRunNoKey.getHttpResultCode());

        // ----------------- Run -----------------
        Response<Trace> responseRunWebhook = syncano.runWebhook(WEBHOOK_NAME).send();

        assertTrue(responseRunWebhook.isSuccess());
        assertNotNull(responseRunWebhook.getData());

        // ----------------- Delete -----------------
        Response<Webhook> responseDeleteWebhook = syncano.deleteWebhook(WEBHOOK_NAME).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteWebhook.getHttpResultCode());

        // ----------------- Get One -----------------
        Response<Webhook> responseGetOneWebhook = syncano.getWebhook(WEBHOOK_NAME).send();

        // After delete, Webhook should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneWebhook.getHttpResultCode());
    }

    public void testWebHooksSimpleCalls() {
        Webhook webhook = new Webhook(WEBHOOK_NAME);
        Response<Trace> respRun = webhook.run();

        assertTrue(respRun.isSuccess());
        assertNotNull(webhook.getTrace());
        assertNotNull(webhook.getOutput());
        assertTrue(webhook.getOutput().contains(EXPECTED_RESULT));
    }

    public void testPublicWebhook() {
        // ----------------- Create public webhook -----------------
        Webhook newWebhook = new Webhook(PUBLIC_WEBHOOK_NAME, codeBox.getId());
        newWebhook.setIsPublic(true);

        Response<Webhook> responseCreateWebhook = syncano.createWebhook(newWebhook).send();
        assertTrue(responseCreateWebhook.isSuccess());
        assertNotNull(responseCreateWebhook.getData());
        Webhook webhook = responseCreateWebhook.getData();

        // ----------------- Run without key-----------------
        Syncano noKeySyncano = new Syncano();
        String url = Constants.PRODUCTION_SERVER_URL + webhook.getLinks().publicLink;
        Response<Trace> responseRunWebhook = noKeySyncano.runWebhookUrl(url).send();
        assertTrue(responseRunWebhook.isSuccess());

        Trace trace = responseRunWebhook.getData();
        assertNotNull(trace);
        assertNotNull(trace.getOutput());
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
    }
}