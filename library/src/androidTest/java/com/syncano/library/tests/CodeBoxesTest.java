package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.choice.TraceStatus;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;

import java.util.List;

public class CodeBoxesTest extends SyncanoApplicationTestCase {

    private static final String EXPECTED_RESULT = "This is message from our Codebox";
    private static final String ARGUMENT_NAME = "argument";
    private static final String ARGUMENT_VALUE = "GRrr";
    private CodeBox codeBox;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String codeBoxLabel = "CodeBox Test";

        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg); console.log(ARGS." + ARGUMENT_NAME + ");";

        CodeBox newCodeBox = new CodeBox();
        newCodeBox.setLabel(codeBoxLabel);
        newCodeBox.setRuntimeName(runtime);
        newCodeBox.setSource(source);

        // ----------------- Create -----------------
        Response<CodeBox> responseCreateCodeBox = syncano.createCodeBox(newCodeBox).send();

        assertTrue(responseCreateCodeBox.isSuccess());
        assertNotNull(responseCreateCodeBox.getData());
        codeBox = responseCreateCodeBox.getData();
    }

    @Override
    protected void tearDown() throws Exception {
        Response<CodeBox> responseCodeBoxDelete = syncano.deleteCodeBox(codeBox.getId()).send();
        assertTrue(responseCodeBoxDelete.isSuccess());
        super.tearDown();
    }

    public void testCodeBoxes() throws InterruptedException {
        // ----------------- Get One -----------------
        Response<CodeBox> responseGetCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        assertTrue(responseGetCodeBox.isSuccess());
        assertNotNull(responseGetCodeBox.getData());
        assertEquals(codeBox.getLabel(), responseGetCodeBox.getData().getLabel());
        assertEquals(codeBox.getRuntimeName(), responseGetCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseGetCodeBox.getData().getSource());

        // ----------------- Update -----------------
        String codeBoxNewName = "CodeBox Test New";
        codeBox.setLabel(codeBoxNewName);
        JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("email", "duke.nukem@gmail.com");
        codeBox.setConfig(jsonConfig);

        Response<CodeBox> responseUpdateCodeBox = syncano.updateCodeBox(codeBox).send();

        assertTrue(responseUpdateCodeBox.isSuccess());
        assertNotNull(responseUpdateCodeBox.getData());
        assertEquals(codeBox.getLabel(), responseUpdateCodeBox.getData().getLabel());
        assertEquals(codeBox.getRuntimeName(), responseUpdateCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseUpdateCodeBox.getData().getSource());
        assertNotNull(responseUpdateCodeBox.getData().getConfig());
        assertNotNull(responseUpdateCodeBox.getData().getConfig().get("email"));


        // ----------------- Get List -----------------
        Response<List<CodeBox>> responseGetCodeBoxes = syncano.getCodeBoxes().send();

        assertNotNull(responseGetCodeBoxes.getData());
        assertTrue("List should contain at least one item.", responseGetCodeBoxes.getData().size() > 0);

        // ----------------- Run -----------------
        Response<Trace> responseRunCodeBox = syncano.runCodeBox(codeBox.getId()).send();

        assertTrue(responseRunCodeBox.isSuccess());
        Trace trace = responseRunCodeBox.getData();
        assertNotNull(trace);

        // ----------------- Result -----------------
        long start = System.currentTimeMillis();
        // wait until codebox finishes execution
        while (System.currentTimeMillis() - start < 5000 && trace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(trace.fetch().isSuccess());
            Thread.sleep(100);
        }
        // first method
        assertNotNull(trace.getOutput());
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
        // second method
        Response<Trace> responseTrace = syncano.getTrace(codeBox.getId(), trace.getId()).send();
        assertTrue(responseTrace.isSuccess());
        Trace result = responseTrace.getData();
        assertNotNull(result);
        assertNotNull(result.getOutput());
        assertTrue(result.getOutput().contains(EXPECTED_RESULT));

        // ----------------- Delete -----------------
        Response<CodeBox> responseDeleteCodeBox = syncano.deleteCodeBox(codeBox.getId()).send();

        assertTrue(responseDeleteCodeBox.isSuccess());

        // ----------------- Get One -----------------
        Response<CodeBox> responseGetOneCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        // After delete, CodeBox should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneCodeBox.getHttpResultCode());
    }

    public void testSimpleCodeBoxMethods() throws InterruptedException {
        CodeBox cbx = new CodeBox(codeBox.getId());
        assertTrue(cbx.run().isSuccess());
        Trace trace = cbx.getTrace();

        long start = System.currentTimeMillis();
        // wait until codebox finishes execution
        while (System.currentTimeMillis() - start < 5000 && trace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(trace.fetch().isSuccess());
            Thread.sleep(100);
        }
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));

        // run codebox with payload
        JsonObject json = new JsonObject();
        json.addProperty(ARGUMENT_NAME, ARGUMENT_VALUE);
        cbx = new CodeBox(codeBox.getId());
        assertTrue(cbx.run(json).isSuccess());
        trace = cbx.getTrace();
        start = System.currentTimeMillis();
        // wait until codebox finishes execution
        while (System.currentTimeMillis() - start < 5000 && trace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(trace.fetch().isSuccess());
            Thread.sleep(100);
        }
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
        assertTrue(trace.getOutput().contains(ARGUMENT_VALUE));
    }
}