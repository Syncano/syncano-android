package com.syncano.android.test.modules.projects;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDelete;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyNew;
import com.syncano.android.lib.modules.projects.ParamsProjectAuthorize;
import com.syncano.android.lib.modules.projects.ParamsProjectDeauthorize;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectGet;
import com.syncano.android.lib.modules.projects.ParamsProjectGetOne;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ParamsProjectUpdate;
import com.syncano.android.lib.modules.projects.ResponseProjectGet;
import com.syncano.android.lib.modules.projects.ResponseProjectGetOne;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectUpdate;
import com.syncano.android.lib.objects.ApiKey;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class ProjectsTest extends AndroidTestCase {

    private static final String API_KEY_ROLE_USER = "user";
    private static final String PROJECT_NAME = "CI Test Project";
    private static final String PROJECT_NAME_UPDATED = "CI Test Project Updated";
    private static final String PERMISSION_READ_DATA = "read_data";

    private Syncano syncano = null;
    private String projectId = null;
    private ApiKey apiKey = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));

        // Create Api Key
        ParamsApiKeyNew paramsApiKeyNew = new ParamsApiKeyNew("test description", API_KEY_ROLE_USER);
        ResponseApiKeyNew responseApiKeyNew =  syncano.apikeyNew(paramsApiKeyNew);
        assertEquals("Failed to create new Api Key", Response.CODE_SUCCESS, (int) responseApiKeyNew.getResultCode());
        apiKey = responseApiKeyNew.getApiKey();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Remove Api Key
        ParamsApiKeyDelete paramsApiKeyDelete = new ParamsApiKeyDelete(apiKey.getId());
        Response responseApiKeyDelete = syncano.apikeyDelete(paramsApiKeyDelete);
        assertEquals("Failed to clean  Api Key", Response.CODE_SUCCESS, (int) responseApiKeyDelete.getResultCode());
    }

    public void testProjects() {
        // Projects New
        ParamsProjectNew paramsProjectNew = new ParamsProjectNew(PROJECT_NAME);
        ResponseProjectNew responseProjectNew = syncano.projectNew(paramsProjectNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectNew.getResultCode());
        projectId = responseProjectNew.getProject().getId();

        // Projects Get
        ParamsProjectGet paramsProjectGet = new ParamsProjectGet();
        ResponseProjectGet responseProjectGet = syncano.projectGet(paramsProjectGet);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectGet.getResultCode());
        assertTrue(responseProjectGet.getProject().length > 0);

        // Projects Get One
        ParamsProjectGetOne paramsProjectGetOne = new ParamsProjectGetOne(projectId);
        ResponseProjectGetOne responseProjectGetOne = syncano.projectGetOne(paramsProjectGetOne);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectGetOne.getResultCode());

        // Projects Update
        ParamsProjectUpdate paramsProjectUpdate = new ParamsProjectUpdate(projectId);
        paramsProjectUpdate.setName(PROJECT_NAME_UPDATED);
        ResponseProjectUpdate responseProjectUpdate = syncano.projectUpdate(paramsProjectUpdate);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectUpdate.getResultCode());
        assertEquals(PROJECT_NAME_UPDATED, responseProjectUpdate.getProject().getName());

        // Projects Authorize
        ParamsProjectAuthorize paramsProjectAuthorize = new ParamsProjectAuthorize(apiKey.getId(), PERMISSION_READ_DATA, projectId);
        Response responseProjectAuthorize = syncano.projectAuthorize(paramsProjectAuthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectAuthorize.getResultCode());

        // Projects Deauthorize
        ParamsProjectDeauthorize paramsProjectDeauthorize = new ParamsProjectDeauthorize(apiKey.getId(), PERMISSION_READ_DATA, projectId);
        Response responseProjectDeauthorize = syncano.projectDeuthorize(paramsProjectDeauthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectDeauthorize.getResultCode());

        // Projects Delete
        ParamsProjectDelete paramsProjectDelete = new ParamsProjectDelete(projectId);
        Response responseProjectDelete = syncano.projectDelete(paramsProjectDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseProjectDelete.getResultCode());
    }
}
