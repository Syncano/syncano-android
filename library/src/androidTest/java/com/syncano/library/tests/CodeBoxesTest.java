package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;

import java.util.List;

public class CodeBoxesTest extends SyncanoApplicationTestCase {

    private static final String EXPECTED_RESULT = "this is message from our Codebox";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testCodeBoxes() throws InterruptedException {

        String codeBoxLabel = "CodeBox Test";
        String codeBoxNewName = "CodeBox Test New";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        final CodeBox newCodeBox = new CodeBox();
        newCodeBox.setLabel(codeBoxLabel);
        newCodeBox.setRuntimeName(runtime);
        newCodeBox.setSource(source);

        CodeBox codeBox;

        // ----------------- Create -----------------
        Response<CodeBox> responseCreateCodeBox = syncano.createCodeBox(newCodeBox).send();

        assertTrue(responseCreateCodeBox.isSuccess());
        assertNotNull(responseCreateCodeBox.getData());
        codeBox = responseCreateCodeBox.getData();

        // ----------------- Get One -----------------
        Response<CodeBox> responseGetCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        assertTrue(responseGetCodeBox.isSuccess());
        assertNotNull(responseGetCodeBox.getData());
        assertEquals(codeBox.getLabel(), responseGetCodeBox.getData().getLabel());
        assertEquals(codeBox.getRuntimeName(), responseGetCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseGetCodeBox.getData().getSource());

        // ----------------- Update -----------------
        codeBox.setLabel(codeBoxNewName);
        Response<CodeBox> responseUpdateCodeBox = syncano.updateCodeBox(codeBox).send();

        assertTrue(responseUpdateCodeBox.isSuccess());
        assertNotNull(responseUpdateCodeBox.getData());
        assertEquals(codeBox.getLabel(), responseUpdateCodeBox.getData().getLabel());
        assertEquals(codeBox.getRuntimeName(), responseUpdateCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseUpdateCodeBox.getData().getSource());

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
        Thread.sleep(2000); // wait until codebox finishes execution
        // first method
        Response<Trace> responseTrace = trace.fetch();
        assertTrue(responseTrace.isSuccess());
        assertTrue(trace.getResult().stdout.contains(EXPECTED_RESULT));
        // second method
        responseTrace = syncano.getTrace(codeBox.getId(), trace.getId()).send();
        assertTrue(responseTrace.isSuccess());
        Trace result = responseTrace.getData();
        assertNotNull(result);
        assertTrue(result.getResult().stdout.contains(EXPECTED_RESULT));

        // ----------------- Delete -----------------
        Response<CodeBox> responseDeleteCodeBox = syncano.deleteCodeBox(codeBox.getId()).send();

        assertTrue(responseDeleteCodeBox.isSuccess());

        // ----------------- Get One -----------------
        Response<CodeBox> responseGetOneCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        // After delete, CodeBox should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneCodeBox.getHttpResultCode());
    }
}