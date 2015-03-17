package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.android.lib.api.Page;
import com.syncano.android.lib.api.Response;
import com.syncano.android.lib.api.SyncanoException;
import com.syncano.android.lib.choice.RuntimeName;
import com.syncano.android.lib.data.CodeBox;
import com.syncano.android.lib.data.RunCodeBoxResult;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class CodeBoxesTest extends ApplicationTestCase<Application> {

    private static final String TAG = CodeBoxesTest.class.getSimpleName();

    private final static int TIMEOUT_MILLIS = 10 * 1000;
    private static final String EXPECTED_RESULT = "this is message from our Codebox";

    private Syncano syncano;

    public CodeBoxesTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCodeBoxes() throws InterruptedException {

        String codeBoxName = "CodeBox Test";
        String codeBoxNewName = "CodeBox Test New";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        final CodeBox newCodeBox = new CodeBox();
        newCodeBox.setName(codeBoxName);
        newCodeBox.setRuntimeName(runtime);
        newCodeBox.setSource(source);

        CodeBox codeBox;

        // ----------------- Create -----------------
        Response <CodeBox> responseCreateCodeBox = syncano.createCodeBox(newCodeBox).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateCodeBox.getHttpResultCode());
        assertNotNull(responseCreateCodeBox.getData());
        codeBox = responseCreateCodeBox.getData();

        // ----------------- Get One -----------------
        Response <CodeBox> responseGetCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetCodeBox.getHttpResultCode());
        assertNotNull(responseGetCodeBox.getData());
        assertEquals(codeBox.getName(), responseGetCodeBox.getData().getName());
        assertEquals(codeBox.getRuntimeName(), responseGetCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseGetCodeBox.getData().getSource());

        // ----------------- Update -----------------
        codeBox.setName(codeBoxNewName);
        Response <CodeBox> responseUpdateCodeBox = syncano.updateCodeBox(codeBox).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateCodeBox.getHttpResultCode());
        assertNotNull(responseUpdateCodeBox.getData());
        assertEquals(codeBox.getName(), responseUpdateCodeBox.getData().getName());
        assertEquals(codeBox.getRuntimeName(), responseUpdateCodeBox.getData().getRuntimeName());
        assertEquals(codeBox.getSource(), responseUpdateCodeBox.getData().getSource());

        // ----------------- Get Page -----------------
        /*lock = new CountDownLatch(1);
        syncano.getCodeBoxes(new GetCallback<Page<CodeBox>>() {
            @Override
            public void success(Page<CodeBox> page) {
                assertNotNull(page);
                assertNotNull(page.getObjects());
                assertTrue("List should contain at least one item.", page.getObjects().size() > 0);
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to get list");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);*/

        // ----------------- Run -----------------
        Response <RunCodeBoxResult> responseRunCodeBox = syncano.runCodeBox(codeBox.getId()).send();

        assertEquals(responseRunCodeBox.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responseRunCodeBox.getHttpResultCode());
        assertNotNull(responseRunCodeBox.getData());

        // ----------------- Delete -----------------
        Response <CodeBox> responseDeleteCodeBox = syncano.deleteCodeBox(codeBox.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteCodeBox.getHttpResultCode());

        // ----------------- Get One -----------------
        Response <CodeBox> responseGetOneCodeBox = syncano.getCodeBox(codeBox.getId()).send();

        // After delete, CodeBox should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneCodeBox.getHttpResultCode());
    }
}