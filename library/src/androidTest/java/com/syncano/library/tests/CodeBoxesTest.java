package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Trace;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
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

        assertEquals(responseCreateCodeBox.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateCodeBox.getHttpResultCode());
        assertNotNull(responseCreateCodeBox.getData());
        codeBox = responseCreateCodeBox.getData();

        // ----------------- Get One -----------------
        Response<CodeBox> responseGetCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetCodeBox.getHttpResultCode());
        assertNotNull(responseGetCodeBox.getData());
        assertEquals(codeBox.getLabel(), responseGetCodeBox.getData().getLabel());
        assertEquals(codeBox.getRuntimeName(), responseGetCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseGetCodeBox.getData().getSource());

        // ----------------- Update -----------------
        codeBox.setLabel(codeBoxNewName);
        Response<CodeBox> responseUpdateCodeBox = syncano.updateCodeBox(codeBox).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateCodeBox.getHttpResultCode());
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

        assertEquals(responseRunCodeBox.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responseRunCodeBox.getHttpResultCode());
        assertNotNull(responseRunCodeBox.getData());

        // ----------------- Delete -----------------
        Response<CodeBox> responseDeleteCodeBox = syncano.deleteCodeBox(codeBox.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteCodeBox.getHttpResultCode());

        // ----------------- Get One -----------------
        Response<CodeBox> responseGetOneCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        // After delete, CodeBox should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneCodeBox.getHttpResultCode());
    }
}