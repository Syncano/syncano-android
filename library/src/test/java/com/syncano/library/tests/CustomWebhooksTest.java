package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
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
import static org.junit.Assert.assertTrue;

public class CustomWebhooksTest extends SyncanoApplicationTestCase {

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
        RuntimeName runtime = RuntimeName.PYTHON;
        String source = "import json\n" +
                "\n" +
                "dictionary = {\n" +
                "    \"mystatus\" : \"OK\",\n" +
                "    \"information about\" : \"Visit us\",\n" +
                "    \"site\" : \"https://www.syncano.io\"\n" +
                "    }\n" +
                "    \n" +
                "content = json.dumps(dictionary)\n" +
                "redirectResponse = HttpResponse(status_code=200, content=content, content_type='text/json');\n" +
                "set_response(redirectResponse);";
        CodeBox newCodeBox = new CodeBox(codeBoxLabel, source, runtime);

        // ----------------- Create CodeBox -----------------
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();

        assertTrue(responseCodeBoxCreate.isSuccess());
        assertNotNull(responseCodeBoxCreate.getData());
        codeBox = responseCodeBoxCreate.getData();

        // ----------------- Delete Webhook -----------------
        Response<CustomWebhook> delResp = syncano.deleteWebhook(WEBHOOK_NAME).send();
        assertTrue(delResp.isSuccess());

        // ----------------- Create Webhook -----------------
        CustomWebhook newWebhook = new CustomWebhook(WEBHOOK_NAME, codeBox.getId());
        Response<CustomWebhook> responseCreateWebhook = syncano.createWebhook(newWebhook).send();

        assertTrue(responseCreateWebhook.isSuccess());
        assertNotNull(responseCreateWebhook.getData());
    }

    @After
    public void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());

        // ----------------- Delete Webhook -----------------
        Response<CustomWebhook> delResp = syncano.deleteWebhook(WEBHOOK_NAME).send();
        assertTrue(delResp.isSuccess());

        super.tearDown();
    }

    @Test
    public void testWebhooks() throws InterruptedException {
        // ----------------- Get One -----------------
        Response<CustomWebhook> responseGetWebhook = syncano.getWebhook(WEBHOOK_NAME).send();

        assertTrue(responseGetWebhook.isSuccess());
        assertNotNull(responseGetWebhook.getData());
        CustomWebhook webhook = responseGetWebhook.getData();
        assertEquals(webhook.getName(), responseGetWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseGetWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseGetWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseGetWebhook.getData().getIsPublic());

        // ----------------- Run -----------------
        Response<CustomWebHookResponse> responseRunWebhook = syncano.runWebhook(CustomWebHookResponse.class, webhook).send();

        assertTrue(responseRunWebhook.isSuccess());
        assertNotNull(responseRunWebhook.getData());

        // ----------------- Delete -----------------
        Response<CustomWebhook> responseDeleteWebhook = syncano.deleteWebhook(WEBHOOK_NAME).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteWebhook.getHttpResultCode());

        // ----------------- Get One -----------------
        Response<CustomWebhook> responseGetOneWebhook = syncano.getWebhook(WEBHOOK_NAME).send();

        // After delete, Webhook should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneWebhook.getHttpResultCode());
    }



    public static class CustomWebHookResponse {
        @SyncanoField(name = "mystatus")
        private String status;
        @SyncanoField(name = "information about")
        private String moreInfo;
        @SyncanoField(name = "site")
        private String site;

    }
}