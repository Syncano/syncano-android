package com.syncano.library.testsv1;

import com.google.gson.JsonObject;
import com.syncano.library.BuildConfig;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.Webhook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WebhooksTest extends SyncanoApplicationTestCase {

    private static final String WEBHOOK_NAME = "webhook_test";
    private static final String PUBLIC_WEBHOOK_NAME = "public_webhook";
    private static final String EXPECTED_RESULT = "This is message from our Codebox";
    private static final String ARGUMENT_NAME = "argument";
    private static final String ARGUMENT_VALUE = "GRrr";
    private CodeBox codeBox;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        String codeBoxLabel = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg); console.log(ARGS);";
        CodeBox newCodeBox = new CodeBox(codeBoxLabel, source, runtime);

        // ----------------- Create CodeBox -----------------
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();

        assertTrue(responseCodeBoxCreate.isSuccess());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();

        // ----------------- Delete Webhook -----------------
        RequestDelete<Webhook> deleteRequest = syncano.deleteWebhook(WEBHOOK_NAME);
        Response<Webhook> delResp = deleteRequest.send();
        assertTrue(delResp.isSuccess());

        deleteRequest = syncano.deleteWebhook(PUBLIC_WEBHOOK_NAME);
        delResp = deleteRequest.send();
        assertTrue(delResp.isSuccess());


        // ----------------- Create Webhook -----------------
        Webhook newWebhook = new Webhook(WEBHOOK_NAME, codeBox.getId());
        Response<Webhook> responseCreateWebhook = syncano.createWebhook(newWebhook).send();
        assertTrue(responseCreateWebhook.isSuccess());
        assertNotNull(responseCreateWebhook.getData());
    }

    @After
    public void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());

        // ----------------- Delete Webhook -----------------
        RequestDelete<Webhook> deleteWebhookRequest = syncano.deleteWebhook(WEBHOOK_NAME);
        Response<Webhook> delResp = deleteWebhookRequest.send();
        assertTrue(delResp.isSuccess());

        super.tearDown();
    }

    @Test
    public void testWebhooks() throws InterruptedException {
        // ----------------- Get One -----------------
        RequestGet<Webhook> requestGetWebHook = syncano.getWebhook(WEBHOOK_NAME);
        Response<Webhook> responseGetWebhook = requestGetWebHook.send();

        assertTrue(responseGetWebhook.isSuccess());
        assertNotNull(responseGetWebhook.getData());
        Webhook webhook = responseGetWebhook.getData();
        assertEquals(webhook.getName(), responseGetWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseGetWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseGetWebhook.getData().getPublicLink());
        assertEquals(webhook.isPublic(), responseGetWebhook.getData().isPublic());

        // ----------------- Update -----------------
        webhook.setCodebox(codeBox.getId());
        Response<Webhook> responseUpdateWebhook = syncano.updateWebhook(webhook).send();

        assertTrue(responseUpdateWebhook.isSuccess());
        assertNotNull(responseUpdateWebhook.getData());
        assertEquals(webhook.getName(), responseUpdateWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseUpdateWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseUpdateWebhook.getData().getPublicLink());
        assertEquals(webhook.isPublic(), responseUpdateWebhook.getData().isPublic());

        // ----------------- Get List -----------------
        ResponseGetList<Webhook> responseGetWebhooks = syncano.getWebhooks().send();

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
        RequestDelete<Webhook> deleteWebhookRequest = syncano.deleteWebhook(WEBHOOK_NAME);
        Response<Webhook> responseDeleteWebhook = deleteWebhookRequest.send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteWebhook.getHttpResultCode());

        // ----------------- Get One -----------------
        RequestGet<Webhook> requestGetOne = syncano.getWebhook(WEBHOOK_NAME);
        Response<Webhook> responseGetOneWebhook = requestGetOne.send();

        // After delete, Webhook should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneWebhook.getHttpResultCode());
    }

    @Test
    public void testWebHooksSimpleCalls() {
        Webhook webhook = new Webhook(WEBHOOK_NAME);
        Response<Trace> respRun = webhook.run();

        assertTrue(respRun.isSuccess());
        assertNotNull(webhook.getTrace());
        assertNotNull(webhook.getOutput());
        assertTrue(webhook.getOutput().contains(EXPECTED_RESULT));

        // test webhook run with payload
        JsonObject json = new JsonObject();
        json.addProperty(ARGUMENT_NAME, ARGUMENT_VALUE);
        respRun = webhook.run(json);

        assertTrue(respRun.isSuccess());
        assertNotNull(webhook.getTrace());
        String output = webhook.getOutput();
        assertNotNull(output);
        assertTrue(output.contains(EXPECTED_RESULT));
        assertTrue(output.contains(ARGUMENT_VALUE));
    }

    @Test
    public void testPublicWebhook() {
        // ----------------- Create public webhook -----------------
        Webhook webhook = new Webhook(PUBLIC_WEBHOOK_NAME, codeBox.getId());
        webhook.setPublic(true);
        assertNull(webhook.getPublicLink());

        Response<Webhook> responseCreateWebhook = syncano.createWebhook(webhook).send();
        assertTrue(responseCreateWebhook.isSuccess());
        assertNotNull(webhook.getPublicLink());


        // ----------------- Run without key-----------------
        Syncano noKeySyncano = new Syncano();
        Response<Trace> responseRunWebhook = noKeySyncano.runWebhookUrl(webhook.getPublicLink()).send();
        assertTrue(responseRunWebhook.isSuccess());

        Trace trace = responseRunWebhook.getData();
        assertNotNull(trace);
        assertNotNull(trace.getOutput());
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
    }
}