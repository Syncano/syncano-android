package com.syncano.library.tests;


import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;
import com.syncano.library.data.Webhook;

public class PublicWebhooksTest extends SyncanoApplicationTestCase {
    private static final String WEBHOOK_NAME = "name01";
    private static final String EXPECTED_RESULT = "This is message from our Codebox";

    private CodeBox codeBox;

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
        Response<Webhook> delResp = syncano.deleteWebhook(WEBHOOK_NAME).send();
        assertTrue(delResp.isSuccess());
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

    public void testPublicWebhook() {
        // ----------------- Create public webhook -----------------
        Webhook newWebhook = new Webhook(WEBHOOK_NAME, codeBox.getId());
        newWebhook.setIsPublic(true);

        Response<Webhook> responseCreateWebhooks = syncano.createWebhook(newWebhook).send();

        assertTrue(responseCreateWebhooks.isSuccess());
        assertNotNull(responseCreateWebhooks.getData());
        Webhook webhook = responseCreateWebhooks.getData();

        // ----------------- Run without key-----------------
        Syncano noKeySyncano = new Syncano();
        String url = Constants.PRODUCTION_SERVER_URL + webhook.getLinks().publicLink;
        Response<Trace> responseRunWebhook = noKeySyncano.runWebhookUrl(url).send();
        assertTrue(responseRunWebhook.isSuccess());
        assertTrue(responseRunWebhook.getData().getResult().stdout.contains(EXPECTED_RESULT));
    }

}
