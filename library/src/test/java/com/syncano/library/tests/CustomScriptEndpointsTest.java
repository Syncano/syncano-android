package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.RequestDelete;
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

public class CustomScriptEndpointsTest extends SyncanoApplicationTestCase {

    private static final String ENDPOINT_NAME = "custom_endpoint_test";
    private static final String KEY = "testKey";
    private static final String VALUE = "testValue";
    private static final String EXPECTED_RESULT = "{\"" + KEY + "\":\"" + VALUE + "\"}";

    private Script script;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // create script
        String source = "set_response(HttpResponse(status_code=200, content='" + EXPECTED_RESULT + "', content_type='text/plain'));";
        Script newScript = new Script("Script custom response", source, RuntimeName.PYTHON);
        Response<Script> responseScriptCreate = syncano.createScript(newScript).send();
        assertTrue(responseScriptCreate.isSuccess());
        assertNotNull(responseScriptCreate.getData());
        script = responseScriptCreate.getData();

        // delete old ScriptEndpoint
        RequestDelete<ScriptEndpoint> deleteRequest = syncano.deleteScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> delResp = deleteRequest.send();
        assertTrue(delResp.isSuccess());

        // create new ScriptEndpoint
        ScriptEndpoint newScriptEndpoint = new ScriptEndpoint(ENDPOINT_NAME, script.getId());
        Response<ScriptEndpoint> responseCreateScriptEndpoint = syncano.createScriptEndpoint(newScriptEndpoint).send();
        assertTrue(responseCreateScriptEndpoint.isSuccess());
        assertNotNull(responseCreateScriptEndpoint.getData());

        // check if script itself runs properly
        Response<Trace> cbResp = script.run();
        Trace cbTrace = cbResp.getData();
        assertNotNull(cbTrace);
        long start = System.currentTimeMillis();
        // wait until script finishes execution
        while (System.currentTimeMillis() - start < 60000 && cbTrace.getStatus() != TraceStatus.SUCCESS) {
            assertTrue(cbTrace.fetch().isSuccess());
            Thread.sleep(1000);
        }
        Trace.TraceResponse cbCustomResult = cbTrace.getResponse();
        assertNotNull(cbCustomResult);
        assertNotNull(cbCustomResult.content);
        assertTrue(cbCustomResult.content.contains(EXPECTED_RESULT));
    }

    @After
    public void tearDown() throws Exception {
        // delete script
        Response<Script> responseScriptDelete = syncano.deleteScript(script.getId()).send();
        assertTrue(responseScriptDelete.isSuccess());

        // delete ScriptEndpoint
        RequestDelete<ScriptEndpoint> deleteScriptEndpointRequest = syncano.deleteScriptEndpoint(ENDPOINT_NAME);
        Response<ScriptEndpoint> delResp = deleteScriptEndpointRequest.send();
        assertTrue(delResp.isSuccess());
        super.tearDown();
    }

    @Test
    public void testStringScriptEndpoints() {
        // run as a custom response ScriptEndpoint
        Response<String> stringResp = syncano.runScriptEndpointCustomResponse(ENDPOINT_NAME).send();
        assertTrue(stringResp.isSuccess());
        assertTrue(stringResp.getData().contains(EXPECTED_RESULT));

        // run with ScriptEndpoint object method
        ScriptEndpoint scriptEndpoint = new ScriptEndpoint(ENDPOINT_NAME);
        scriptEndpoint.runCustomResponse();
        String response = scriptEndpoint.getCustomResponse();
        assertEquals(EXPECTED_RESULT, response);
    }

    @Test
    public void testCustomScriptEndpoint() {
        // run as a custom response scriptEndpoint
        Response<MyResponse> customResp = syncano.runScriptEndpointCustomResponse(ENDPOINT_NAME, MyResponse.class).send();
        assertTrue(customResp.isSuccess());
        MyResponse myResp = customResp.getData();
        assertNotNull(myResp);
        assertEquals(VALUE, myResp.someParam);

        // run with scriptEndpoint object method
        ScriptEndpoint scriptEndpoint = new ScriptEndpoint(ENDPOINT_NAME);
        scriptEndpoint.runCustomResponse(MyResponse.class);
        myResp = scriptEndpoint.getCustomResponse();
        assertNotNull(myResp);
        assertEquals(VALUE, myResp.someParam);
    }

    private static class MyResponse {
        @SyncanoField(name = KEY)
        String someParam;
    }
}