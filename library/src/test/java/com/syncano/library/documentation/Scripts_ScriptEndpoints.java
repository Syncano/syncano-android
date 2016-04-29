package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.choice.TraceStatus;
import com.syncano.library.data.Script;
import com.syncano.library.data.ScriptEndpoint;
import com.syncano.library.data.Trace;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Scripts_ScriptEndpoints extends SyncanoApplicationTestCase {
    private static final String EXPECTED_RESULT = "this is message from our script";
    private static final String ENDPOINT_NAME = "endpoint_name";
    private int scriptId;
    private String url;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // create script
        String label = "Script Test";
        RuntimeName runtime = RuntimeName.NODEJS;
        String source = "var msg = '" + EXPECTED_RESULT + "'; console.log(msg);";

        Script newScript = new Script();
        newScript.setLabel(label);
        newScript.setRuntimeName(runtime);
        newScript.setSource(source);

        Response<Script> responseCreateScript = syncano.createScript(newScript).send();

        assertEquals(responseCreateScript.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateScript.getHttpResultCode());
        assertNotNull(responseCreateScript.getData());

        scriptId = responseCreateScript.getData().getId();

        //create endpoint
        Response<ScriptEndpoint> responseDeleteScriptEndpoint = syncano.deleteScriptEndpoint(ENDPOINT_NAME).send();
        assertTrue(responseDeleteScriptEndpoint.isSuccess());

        ScriptEndpoint endpoint = new ScriptEndpoint(ENDPOINT_NAME, scriptId);
        endpoint.setPublic(true);
        Response<ScriptEndpoint> responseCreateEndpoint = syncano.createScriptEndpoint(endpoint).send();
        assertTrue(responseCreateEndpoint.isSuccess());
        ScriptEndpoint createdEndpoint = responseCreateEndpoint.getData();
        url = createdEndpoint.getPublicLink();
    }

    @After
    public void tearDown() throws Exception {
        syncano.deleteScript(scriptId).send();
        super.tearDown();
    }

    @Test
    public void testRunCodebox() throws InterruptedException {
        // ---------- Running

        JsonObject params = new JsonObject();
        params.addProperty("first_name", "Ryan");
        params.addProperty("last_name", "Gosling");

        Response<Trace> response = syncano.runScript(scriptId, params).send();
        Trace trace = response.getData();

        // -----------------------------

        assertTrue(response.isSuccess());
        Thread.sleep(1000);

        // ---------- Getting result
        trace.fetch();

        if (trace.getStatus() == TraceStatus.SUCCESS) {
            trace.getOutput();
        }
        // -----------------------------
    }

    @Test
    public void testRunEndpoint() throws InterruptedException {
        //
        ScriptEndpoint endpoint = new ScriptEndpoint("endpoint_name");
        endpoint.run();
        String output = endpoint.getOutput();
        //

        //
        // url is a full url
        Response<Trace> response = syncano.runScriptEndpointUrl(url).send();
        Trace trace = response.getData();
        //
    }

    @Test
    public void testRunUrlWithData() throws InterruptedException {
        //
        JsonObject params = new JsonObject();
        params.addProperty("message", "Hello World!");
        // url is a full url
        Response<Trace> response = syncano.runScriptEndpointUrl(url, params).send();
        Trace trace = response.getData();
        //
    }
}
