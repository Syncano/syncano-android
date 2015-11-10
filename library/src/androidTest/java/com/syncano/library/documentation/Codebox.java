package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;

public class Codebox extends SyncanoApplicationTestCase {
    private static final String EXPECTED_RESULT = "this is message from our Codebox";
    private int codeboxId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String codeBoxLabel = "CodeBox Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        final CodeBox newCodeBox = new CodeBox();
        newCodeBox.setLabel(codeBoxLabel);
        newCodeBox.setRuntimeName(runtime);
        newCodeBox.setSource(source);

        Response<CodeBox> responseCreateCodeBox = syncano.createCodeBox(newCodeBox).send();

        assertEquals(responseCreateCodeBox.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateCodeBox.getHttpResultCode());
        assertNotNull(responseCreateCodeBox.getData());

        codeboxId = responseCreateCodeBox.getData().getId();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        syncano.deleteCodeBox(codeboxId).send();
    }

    public void testRunCodebox() {
        // ---------- Running CodeBoxes

        JsonObject params = new JsonObject();
        params.addProperty("first_name", "Ryan");
        params.addProperty("last_name", "Gosling");

        Response<Trace> response = syncano.runCodeBox(codeboxId, params).send();

        // -----------------------------

        assertEquals(response.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }
}
