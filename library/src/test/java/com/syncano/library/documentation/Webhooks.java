package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.Webhook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Webhooks extends SyncanoApplicationTestCase {
    private static final String EXPECTED_RESULT = "this is message from our Codebox";
    private static final String WEBHOOK_NAME = "webhook_name";
    private int codeboxId;
    private String url;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // create codebox
        String codeBoxLabel = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        CodeBox newCodeBox = new CodeBox();
        newCodeBox.setLabel(codeBoxLabel);
        newCodeBox.setRuntimeName(runtime);
        newCodeBox.setSource(source);

        Response<CodeBox> responseCreateCodeBox = syncano.createCodeBox(newCodeBox).send();

        assertEquals(responseCreateCodeBox.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateCodeBox.getHttpResultCode());
        assertNotNull(responseCreateCodeBox.getData());

        codeboxId = responseCreateCodeBox.getData().getId();

        // create webhook
        Response<Webhook> responseDeleteWebhook = syncano.deleteWebhook(WEBHOOK_NAME).send();
        assertTrue(responseDeleteWebhook.isSuccess());

        Webhook webhook = new Webhook(WEBHOOK_NAME, codeboxId);
        webhook.setPublic(true);
        Response<Webhook> responseCreateWebhook = syncano.createWebhook(webhook).send();
        assertTrue(responseCreateWebhook.isSuccess());
        Webhook createdWebhook = responseCreateWebhook.getData();
        url = createdWebhook.getPublicLink();
    }

    @After
    public void tearDown() throws Exception {
        syncano.deleteWebhook(WEBHOOK_NAME).send();
        syncano.deleteCodeBox(codeboxId).send();
        super.tearDown();
    }

    @Test
    public void testWebhooks() {
        // ---------- Let's run the webhook to test if everything works
        Response<Trace> response = syncano.runWebhook(WEBHOOK_NAME).send();
        Trace trace = response.getData();
        String output = trace.getOutput();
        // -----------------------------

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(trace);

        // ---------- Run with payload
        JsonObject payload = new JsonObject();
        payload.addProperty("user_search", "dancing");
        Response<Trace> responsePayload = syncano.runWebhook(WEBHOOK_NAME, payload).send();
        Trace tracePayload = responsePayload.getData();
        String outputPayload = tracePayload.getOutput();
        // -----------------------------
    }

    @Test
    public void testPublicWebhooks() {
        // ---------- Public Webhooks
        Response<Trace> responsePublic = syncano.runWebhookUrl(url + "?message=Hello").send();
        // -----------------------------

        assertEquals(responsePublic.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responsePublic.getHttpResultCode());

        // ---------- Public Webhooks POST
        JsonObject payload = new JsonObject();
        payload.addProperty("message", "Hello World!");

        Response<Trace> response = syncano.runWebhookUrl(url, payload).send();
        // -----------------------------

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }
}
