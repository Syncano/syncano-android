package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.BuildConfig;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.RequestDelete;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.Script;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.Trace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ScriptEndpointsTest extends SyncanoApplicationTestCase {

    private static final String ENDPOINT_NAME = "endpoint_test";
    private static final String PUBLIC_ENDPOINT_NAME = "public_endpoint";
    private static final String EXPECTED_RESULT = "This is message from our script";
    private static final String ARGUMENT_NAME = "argument";
    private static final String ARGUMENT_VALUE = "GRrr";
    private Script script;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg); console.log(ARGS);";
        Script newScript = new Script("Script Test", source, runtime);

        // ----------------- Create Script -----------------
        Response<Script> responseCreate = syncano.createScript(newScript).send();

        assertTrue(responseCreate.isSuccess());
        assertNotNull(responseCreate.getData());
        script = responseCreate.getData();

        // ----------------- Delete ScriptEndpoint -----------------
        RequestDelete<ScriptEndpoint> deleteRequest = syncano.deleteScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> delResp = deleteRequest.send();
        assertTrue(delResp.isSuccess());

        deleteRequest = syncano.deleteScriptEndpoint(PUBLIC_ENDPOINT_NAME);
        delResp = deleteRequest.send();
        assertTrue(delResp.isSuccess());


        // ----------------- Create ScriptEndpoint -----------------
        ScriptEndpoint newEndpoint = new ScriptEndpoint(ENDPOINT_NAME, script.getId());
        Response<ScriptEndpoint> responseCreateEndpoint = syncano.createScriptEndpoint(newEndpoint).send();
        assertTrue(responseCreateEndpoint.isSuccess());
        assertNotNull(responseCreateEndpoint.getData());
    }

    @After
    public void tearDown() throws Exception {
        // ----------------- Delete Script -----------------
        Response<Script> responseScriptDelete = syncano.deleteScript(script.getId()).send();
        assertTrue(responseScriptDelete.isSuccess());

        // ----------------- Delete ScriptEndpoint -----------------
        RequestDelete<ScriptEndpoint> deleteEndpointRequest = syncano.deleteScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> delResp = deleteEndpointRequest.send();
        assertTrue(delResp.isSuccess());

        super.tearDown();
    }

    @Test
    public void testEndpoints() throws InterruptedException {
        // ----------------- Get One -----------------
        RequestGet<ScriptEndpoint> requestGetScriptEndpoint = syncano.getScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> responseGetScriptEndpoint = requestGetScriptEndpoint.send();

        assertTrue(responseGetScriptEndpoint.isSuccess());
        assertNotNull(responseGetScriptEndpoint.getData());
        ScriptEndpoint endpoint = responseGetScriptEndpoint.getData();
        assertEquals(endpoint.getName(), responseGetScriptEndpoint.getData().getName());
        assertEquals(endpoint.getScript(), responseGetScriptEndpoint.getData().getScript());
        assertEquals(endpoint.getPublicLink(), responseGetScriptEndpoint.getData().getPublicLink());
        assertEquals(endpoint.isPublic(), responseGetScriptEndpoint.getData().isPublic());

        // ----------------- Update -----------------
        endpoint.setScript(script.getId());
        Response<ScriptEndpoint> responseUpdateEndpoint = syncano.updateScriptEndpoint(endpoint).send();

        assertTrue(responseUpdateEndpoint.isSuccess());
        assertNotNull(responseUpdateEndpoint.getData());
        assertEquals(endpoint.getName(), responseUpdateEndpoint.getData().getName());
        assertEquals(endpoint.getScript(), responseUpdateEndpoint.getData().getScript());
        assertEquals(endpoint.getPublicLink(), responseUpdateEndpoint.getData().getPublicLink());
        assertEquals(endpoint.isPublic(), responseUpdateEndpoint.getData().isPublic());

        // ----------------- Get List -----------------
        ResponseGetList<ScriptEndpoint> responseGetScriptEndpoints = syncano.getScriptEndpoints().send();

        assertNotNull(responseGetScriptEndpoints.getData());
        assertTrue("List should contain at least one item.", responseGetScriptEndpoints.getData().size() > 0);

        // ----------------- Run without key-----------------
        Syncano noKeySyncano = new Syncano(BuildConfig.INSTANCE_NAME);
        Response<Trace> responseRunNoKey = noKeySyncano.runScriptEndpoint(ENDPOINT_NAME).send();

        assertEquals(responseRunNoKey.getHttpReasonPhrase(), Response.HTTP_CODE_FORBIDDEN, responseRunNoKey.getHttpResultCode());

        // ----------------- Run -----------------
        Response<Trace> responseRunEndpoint = syncano.runScriptEndpoint(ENDPOINT_NAME).send();

        assertTrue(responseRunEndpoint.isSuccess());
        assertNotNull(responseRunEndpoint.getData());

        // ----------------- Delete -----------------
        RequestDelete<ScriptEndpoint> deleteScriptEndpoint = syncano.deleteScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> responseDeleteScriptEndpoint = deleteScriptEndpoint.send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteScriptEndpoint.getHttpResultCode());

        // ----------------- Get One -----------------
        RequestGet<ScriptEndpoint> requestGetOne = syncano.getScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> responseGetOneScriptEndpoint = requestGetOne.send();

        // After delete, ScriptEndpoint should not be found.
        assertEquals(Response.HTTP_CODE_NOT_FOUND, responseGetOneScriptEndpoint.getHttpResultCode());
    }

    @Test
    public void testEndpointsSimpleCalls() {
        ScriptEndpoint endpoint = new ScriptEndpoint(ENDPOINT_NAME);
        Response<Trace> respRun = endpoint.run();

        assertTrue(respRun.isSuccess());
        assertNotNull(endpoint.getTrace());
        assertNotNull(endpoint.getOutput());
        assertTrue(endpoint.getOutput().contains(EXPECTED_RESULT));

        // test endpoint run with payload
        JsonObject json = new JsonObject();
        json.addProperty(ARGUMENT_NAME, ARGUMENT_VALUE);
        respRun = endpoint.run(json);

        assertTrue(respRun.isSuccess());
        assertNotNull(endpoint.getTrace());
        String output = endpoint.getOutput();
        assertNotNull(output);
        assertTrue(output.contains(EXPECTED_RESULT));
        assertTrue(output.contains(ARGUMENT_VALUE));
    }

    @Test
    public void testPublicEndpoint() {
        // ----------------- Create public endpoint -----------------
        ScriptEndpoint endpoint = new ScriptEndpoint(PUBLIC_ENDPOINT_NAME, script.getId());
        endpoint.setPublic(true);
        assertNull(endpoint.getPublicLink());

        Response<ScriptEndpoint> responseCreateEndpoint = syncano.createScriptEndpoint(endpoint).send();
        assertTrue(responseCreateEndpoint.isSuccess());
        assertNotNull(endpoint.getPublicLink());


        // ----------------- Run without key-----------------
        Syncano noKeySyncano = new Syncano();
        Response<Trace> responseRunEndpoint = noKeySyncano.runScriptEndpointUrl(endpoint.getPublicLink()).send();
        assertTrue(responseRunEndpoint.isSuccess());

        Trace trace = responseRunEndpoint.getData();
        assertNotNull(trace);
        assertNotNull(trace.getOutput());
        assertTrue(trace.getOutput().contains(EXPECTED_RESULT));
    }
}