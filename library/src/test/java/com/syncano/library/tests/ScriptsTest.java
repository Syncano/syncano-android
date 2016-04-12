package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.choice.TraceStatus;
import com.syncano.library.data.Script;
import com.syncano.library.data.Trace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ScriptsTest extends SyncanoApplicationTestCase {

    private static final String EXPECTED_RESULT = "This is message from our Script";
    private static final String ARGUMENT_NAME = "argument";
    private static final String ARGUMENT_VALUE = "GRrr";
    private Script script;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        String label = "Script Test";

        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg); console.log(ARGS." + ARGUMENT_NAME + ");";

        Script newScript = new Script();
        newScript.setLabel(label);
        newScript.setRuntimeName(runtime);
        newScript.setSource(source);

        // ----------------- Create -----------------
        Response<Script> responseCreateScript = syncano.createScript(newScript).send();

        assertTrue(responseCreateScript.isSuccess());
        assertNotNull(responseCreateScript.getData());
        script = responseCreateScript.getData();
    }

    @After
    public void tearDown() throws Exception {
        Response<Script> responseScriptDelete = syncano.deleteScript(script.getId()).send();
        assertTrue(responseScriptDelete.isSuccess());
        super.tearDown();
    }

    @Test
    public void testScripts() throws InterruptedException {
        // ----------------- Get One -----------------
        Response<Script> responseGetScript = syncano.getScript(script.getId()).send();

        assertTrue(responseGetScript.isSuccess());
        assertNotNull(responseGetScript.getData());
        assertEquals(script.getLabel(), responseGetScript.getData().getLabel());
        assertEquals(script.getRuntimeName(), responseGetScript.getData().getRuntimeName());
        assertEquals(script.getSource(), responseGetScript.getData().getSource());

        // ----------------- Update -----------------
        String newName = "Script Test New";
        script.setLabel(newName);
        JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("email", "duke.nukem@gmail.com");
        script.setConfig(jsonConfig);

        Response<Script> responseUpdate = syncano.updateScript(script).send();

        assertTrue(responseUpdate.isSuccess());
        assertNotNull(responseUpdate.getData());
        assertEquals(script.getLabel(), responseUpdate.getData().getLabel());
        assertEquals(script.getRuntimeName(), responseUpdate.getData().getRuntimeName());
        assertEquals(script.getSource(), responseUpdate.getData().getSource());
        assertNotNull(responseUpdate.getData().getConfig());
        assertNotNull(responseUpdate.getData().getConfig().get("email"));


        // ----------------- Get List -----------------
        Response<List<Script>> responseGetList = syncano.getScripts().send();

        assertNotNull(responseGetList.getData());
        assertTrue("List should contain at least one item.", responseGetList.getData().size() > 0);

        // ----------------- Run -----------------
        Response<Trace> responseRun = syncano.runScript(script.getId()).send();

        assertTrue(responseRun.isSuccess());
        Trace trace = responseRun.getData();
        assertNotNull(trace);

        // ----------------- Result -----------------
        long start = System.currentTimeMillis();
        // wait until script finishes execution
        while (System.currentTimeMillis() - start < 5000 && trace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(trace.fetch().isSuccess());
            Thread.sleep(100);
        }
        // first method
        assertNotNull(trace.getOutput());
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
        // second method
        Response<Trace> responseTrace = syncano.getTrace(script.getId(), trace.getId()).send();
        assertTrue(responseTrace.isSuccess());
        Trace result = responseTrace.getData();
        assertNotNull(result);
        assertNotNull(result.getOutput());
        assertTrue(result.getOutput().contains(EXPECTED_RESULT));

        // ----------------- Delete -----------------
        Response<Script> responseDelete = syncano.deleteScript(script.getId()).send();

        assertTrue(responseDelete.isSuccess());

        // ----------------- Get One -----------------
        Response<Script> responseGetOneScript = syncano.getScript(script.getId()).send();

        // After delete, script should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneScript.getHttpResultCode());
    }

    @Test
    public void testSimpleMethods() throws InterruptedException {
        Script cbx = new Script(script.getId());
        assertTrue(cbx.run().isSuccess());
        Trace trace = cbx.getTrace();

        long start = System.currentTimeMillis();
        // wait until Script finishes execution
        while (System.currentTimeMillis() - start < 5000 && trace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(trace.fetch().isSuccess());
            Thread.sleep(100);
        }
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));

        // run Script with payload
        JsonObject json = new JsonObject();
        json.addProperty(ARGUMENT_NAME, ARGUMENT_VALUE);
        cbx = new Script(script.getId());
        assertTrue(cbx.run(json).isSuccess());
        trace = cbx.getTrace();
        start = System.currentTimeMillis();
        // wait until Script finishes execution
        while (System.currentTimeMillis() - start < 5000 && trace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(trace.fetch().isSuccess());
            Thread.sleep(100);
        }
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
        assertTrue(trace.getOutput().contains(ARGUMENT_VALUE));
    }
}