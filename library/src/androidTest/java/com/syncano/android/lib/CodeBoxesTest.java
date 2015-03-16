package com.syncano.android.lib;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.android.lib.api.Page;
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
    private CountDownLatch lock;

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

        /*String codeBoxName = "CodeBox Test";
        String codeBoxNewName = "CodeBox Test New";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        final CodeBox codeBox = new CodeBox();
        codeBox.setName(codeBoxName);
        codeBox.setRuntimeName(runtime);
        codeBox.setSource(source);

        final WeakReference<CodeBox> resultRef = new WeakReference<>();

        // ----------------- Create -----------------
        lock = new CountDownLatch(1);
        syncano.createCodeBox(codeBox, new GetCallback<CodeBox>() {

            @Override
            public void success(CodeBox object) {
                assertNotNull(object);
                resultRef.value = object;
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to create object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get One -----------------
        lock = new CountDownLatch(1);
        syncano.getCodeBox(resultRef.value.getId(), new GetCallback<CodeBox>() {
            @Override
            public void success(CodeBox object) {
                assertNotNull(object);
                assertEquals(resultRef.value.getName(), object.getName());
                assertEquals(resultRef.value.getRuntimeName(), object.getRuntimeName());
                assertEquals(resultRef.value.getSource(), object.getSource());
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to get object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Update -----------------
        resultRef.value.setName(codeBoxNewName);
        lock = new CountDownLatch(1);
        syncano.updateCodeBox(resultRef.value, new GetCallback<CodeBox>() {
            @Override
            public void success(CodeBox object) {
                assertNotNull(object);
                assertEquals(resultRef.value.getName(), object.getName());
                assertEquals(resultRef.value.getRuntimeName(), object.getRuntimeName());
                assertEquals(resultRef.value.getSource(), object.getSource());
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to update object.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get Page -----------------
        lock = new CountDownLatch(1);
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
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Run -----------------
        lock = new CountDownLatch(1);
        syncano.runCodeBox(resultRef.value.getId(), new GetCallback<RunCodeBoxResult>() {
            @Override
            public void success(RunCodeBoxResult object) {
                assertNotNull(object);
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to run CodeBox.");
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Delete -----------------
        lock = new CountDownLatch(1);
        syncano.deleteCodeBox(resultRef.value.getId(), new DeleteCallback() {
            @Override
            public void success() {
                lock.countDown();
            }

            @Override
            public void failure(SyncanoException error) {
                fail("Failed to delete object. " + error.getHttpResultCode());
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);

        // ----------------- Get One -----------------
        lock = new CountDownLatch(1);
        syncano.getCodeBox(resultRef.value.getId(), new GetCallback<CodeBox>() {
            @Override
            public void success(CodeBox object) {
                assertNull("Failed to delete.", object);
            }

            @Override
            public void failure(SyncanoException error) {
                lock.countDown();
            }
        });
        lock.await(TIMEOUT_MILLIS, TimeUnit.MICROSECONDS);*/
    }
}