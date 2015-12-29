package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.CustomWebhook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Webhooks extends SyncanoApplicationTestCase {
    private static final String EXPECTED_RESULT = "this is message from our Codebox";
    private static final String WEBHOOK_NAME = "webhook_name";
    private int codeboxId;

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
        syncano.deleteWebhook(WEBHOOK_NAME).send();
        CustomWebhook webhook = new CustomWebhook(WEBHOOK_NAME, codeboxId);
        syncano.createWebhook(webhook).send();
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
        Response<Trace> response = syncano.runWebhook("webhook_name").send();
        Trace trace = response.getData();
        // -----------------------------

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        assertNotNull(trace);
    }

    @Test
    public void testWebhooksWithPayload() {
        // ---------- If you wanted to use POST, you can pass extra arguments for the Webhook as a JSON body
        JsonObject params = new JsonObject();
        params.addProperty("first_name", "Ryan");
        params.addProperty("last_name", "Gosling");

     //   Response<Trace> response = syncano.runWebhook("webhook_name", params).send();
        // -----------------------------

      //  assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }
}
