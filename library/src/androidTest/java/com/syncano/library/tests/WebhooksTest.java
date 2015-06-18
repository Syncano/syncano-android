package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.Webhook;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class WebhooksTest extends SyncanoApplicationTestCase {

    private static final String TAG = WebhooksTest.class.getSimpleName();

    private CodeBox codeBox;

    private static final String NAME = "name01";
    private static final String EXPECTED_RESULT = "this is message from our Codebox";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        String codeBoxLabel = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        final CodeBox newCodeBox = new CodeBox(codeBoxLabel, source, runtime);

        // ----------------- Create CodeBox -----------------
        Response<CodeBox> responseCodeBoxCreate = syncano.createCodeBox(newCodeBox).send();

        assertEquals(responseCodeBoxCreate.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCodeBoxCreate.getHttpResultCode());
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
        assertTrue(responseCodeBoxDelete.isOk());
    }

    public void testWebhooks() throws InterruptedException {

        final Webhook newWebhook = new Webhook(NAME, 1);
        Webhook webhook;

        // ----------------- Create -----------------
        Response <Webhook> responseCreateWebhooks = syncano.createWebhook(newWebhook).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateWebhooks.getHttpResultCode());
        assertNotNull(responseCreateWebhooks.getData());
        webhook = responseCreateWebhooks.getData();

        // ----------------- Get One -----------------
        Response <Webhook> responseGetWebhook = syncano.getWebhook(NAME).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetWebhook.getHttpResultCode());
        assertNotNull(responseGetWebhook.getData());
        assertEquals(webhook.getName(), responseGetWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseGetWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseGetWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseGetWebhook.getData().getIsPublic());

        // ----------------- Update -----------------
        webhook.setCodebox(codeBox.getId());
        Response <Webhook> responseUpdateWebhook = syncano.updateWebhook(webhook).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateWebhook.getHttpResultCode());
        assertNotNull(responseUpdateWebhook.getData());
        assertEquals(webhook.getName(), responseUpdateWebhook.getData().getName());
        assertEquals(webhook.getCodebox(), responseUpdateWebhook.getData().getCodebox());
        assertEquals(webhook.getPublicLink(), responseUpdateWebhook.getData().getPublicLink());
        assertEquals(webhook.getIsPublic(), responseUpdateWebhook.getData().getIsPublic());

        // ----------------- Get List -----------------
        Response <List<Webhook>> responseGetWebhooks = syncano.getWebhooks().send();

        assertNotNull(responseGetWebhooks.getData());
        assertTrue("List should contain at least one item.", responseGetWebhooks.getData().size() > 0);

        // ----------------- Run -----------------
        Response<Trace> responseRunWebhook = syncano.runWebhook(NAME, null).send();

        assertEquals(responseRunWebhook.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responseRunWebhook.getHttpResultCode());
        assertNotNull(responseRunWebhook.getData());

        // ----------------- Delete -----------------
        Response <Webhook> responseDeleteWebhook = syncano.deleteWebhook(NAME).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteWebhook.getHttpResultCode());

        // ----------------- Get One -----------------
        Response <Webhook> responseGetOneWebhook = syncano.getWebhook(NAME).send();

        // After delete, Webhook should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneWebhook.getHttpResultCode());
    }
}