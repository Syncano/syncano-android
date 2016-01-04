package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.choice.TraceStatus;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Codebox extends SyncanoApplicationTestCase {
    private static final String EXPECTED_RESULT = "this is message from our Codebox";
    private int codeboxId;

    @Before
    public void setUp() throws Exception {
        super.setUp();
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
    }

    @After
    public void tearDown() throws Exception {
        syncano.deleteCodeBox(codeboxId).send();
        super.tearDown();
    }

    @Test
    public void testRunCodebox() throws InterruptedException {
        // ---------- Running CodeBoxes

        JsonObject params = new JsonObject();
        params.addProperty("first_name", "Ryan");
        params.addProperty("last_name", "Gosling");

        Response<Trace> response = syncano.runCodeBox(codeboxId, params).send();

        // -----------------------------

        assertTrue(response.isSuccess());
        Thread.sleep(1000);

        // ---------- Getting result
        Trace trace = response.getData();
        trace.fetch();

        if (trace.getStatus() == TraceStatus.SUCCESS) {
            trace.getOutput();
        }
        // -----------------------------
    }
}
