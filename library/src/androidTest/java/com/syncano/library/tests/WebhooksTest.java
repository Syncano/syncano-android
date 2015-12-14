package com.syncano.library.tests;

import com.syncano.library.BuildConfig;
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

    private static final String NAME = "name01";
    private static final String EXPECTED_RESULT = "this is message from our Codebox";

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
        // Make sure slug is not taken.
        syncano.deleteWebhook(NAME).send();
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete CodeBox -----------------
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());
    }

    public void testWebhooks() throws InterruptedException {

        Webhook newWebhook = new Webhook(NAME, codeBox.getId());
        Webhook webhook;

        // ----------------- Create -----------------
        Response<Webhook> responseCreateWebhooks = syncano.createWebhook(newWebhook).send();

        assertTrue(responseCreateWebhooks.isSuccess());
        assertNotNull(responseCreateWebhooks.getData());
        webhook = responseCreateWebhooks.getData();

        // ----------------- Get One -----------------
        Response<Webhook> responseGetWebhook = syncano.getWebhook(NAME).send();

        assertTrue(responseGetWebhook.isSuccess());
        assertNotNull(responseGetWebhook.getData());
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
        Response<Trace> responseRunNoKey = noKeySyncano.runWebhook(NAME).send();

        assertEquals(responseRunNoKey.getHttpReasonPhrase(), Response.HTTP_CODE_FORBIDDEN, responseRunNoKey.getHttpResultCode());

        // ----------------- Run -----------------
        Response<Trace> responseRunWebhook = syncano.runWebhook(NAME).send();

        assertTrue(responseRunWebhook.isSuccess());
        assertNotNull(responseRunWebhook.getData());

        // ----------------- Delete -----------------
        Response<Webhook> responseDeleteWebhook = syncano.deleteWebhook(NAME).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteWebhook.getHttpResultCode());

        // ----------------- Get One -----------------
        Response<Webhook> responseGetOneWebhook = syncano.getWebhook(NAME).send();

        // After delete, Webhook should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneWebhook.getHttpResultCode());
    }
}