package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.choice.TraceStatus;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.Webhook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CustomWebhooksTest extends SyncanoApplicationTestCase {

    private static final String WEBHOOK_NAME = "custom_webhook_test";
    private static final String EXPECTED_RESULT = "This is message from our Codebox";

    private CodeBox codeBox;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // create codebox
        String source = "set_response(HttpResponse(status_code=200, content='" + EXPECTED_RESULT + "', content_type='text/plain'));";
        CodeBox newCodeBox = new CodeBox("Codebox custom response", source, RuntimeName.PYTHON);
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();
        assertTrue(responseCodeBoxCreate.isSuccess());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();

        // delete old webhook
        RequestDelete<Webhook> deleteRequest = syncano.deleteWebhook(WEBHOOK_NAME);
        Response<Webhook> delResp = deleteRequest.send();
        assertTrue(delResp.isSuccess());

        // create new webhook
        Webhook newWebhook = new Webhook(WEBHOOK_NAME, codeBox.getId());
        Response<Webhook> responseCreateWebhook = syncano.createWebhook(newWebhook).send();
        assertTrue(responseCreateWebhook.isSuccess());
        assertNotNull(responseCreateWebhook.getData());
    }

    @After
    public void tearDown() throws Exception {
        // delete codebox
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());

        // delete webhook
        RequestDelete<Webhook> deleteWebhookRequest = syncano.deleteWebhook(WEBHOOK_NAME);
        Response<Webhook> delResp = deleteWebhookRequest.send();
        assertTrue(delResp.isSuccess());
        super.tearDown();
    }

    @Test
    public void testCustomWebhooks() throws InterruptedException {
        // first check if codebox itself runs properly
        Response<Trace> cbResp = codeBox.run();
        Trace cbTrace = cbResp.getData();
        assertNotNull(cbTrace);
        long start = System.currentTimeMillis();
        // wait until codebox finishes execution
        while (System.currentTimeMillis() - start < 10000 && cbTrace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(cbTrace.fetch().isSuccess());
            Thread.sleep(100);
        }
        Trace.TraceResponse cbCustomResult = cbTrace.getResponse();
        assertNotNull(cbCustomResult);
        assertNotNull(cbCustomResult.content);
        assertTrue(cbCustomResult.content.contains(EXPECTED_RESULT));

        // run as a normal webhook
        Response<Trace> traceResp = syncano.runWebhook(WEBHOOK_NAME).send();
        assertEquals(Response.CODE_PARSING_RESPONSE_EXCEPTION, traceResp.getResultCode());

        // run as a custom response webhook
        Response<String> stringResp = syncano.runWebhookCustomResponse(WEBHOOK_NAME).send();
        assertTrue(stringResp.isSuccess());
        assertTrue(stringResp.getData().contains(EXPECTED_RESULT));

        // run with webhook object method
        Webhook webhook = new Webhook(WEBHOOK_NAME);
        webhook.runCustomResponse();
        assertTrue(webhook.getCustomResponse().contains(EXPECTED_RESULT));
    }
}