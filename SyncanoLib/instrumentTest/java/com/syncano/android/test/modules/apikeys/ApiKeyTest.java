package com.syncano.android.test.modules.apikeys;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyAuthorize;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDeauthorize;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDelete;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyGet;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyGetOne;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyStartSession;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyUpdateDescription;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyGet;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyGetOne;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyStartSession;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyUpdateDescription;
import com.syncano.android.lib.objects.ApiKey;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class ApiKeyTest extends AndroidTestCase {

    private static final String API_KEY_ROLE_USER = "user";
    private static final String API_DESCRIPTION = "Description";
    private static final String API_DESCRIPTION_UPDATED = "Updated Description";
    private static final String PERMISSION_SEND_NOTIFICATION = "send_notification";

    private Syncano syncano = null;
    private ApiKey apiKey = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testApiKeys() {

        // Api Key Start Session
        ParamsApiKeyStartSession paramsApiKeyStartSession = new ParamsApiKeyStartSession();
        ResponseApiKeyStartSession responseApiKeyStartSession = syncano.apikeyStartSession(paramsApiKeyStartSession);

        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyStartSession.getResultCode());
        assertNotNull(responseApiKeyStartSession.getSessionId());

        // Api Key New
        ParamsApiKeyNew paramsApiKeyNew = new ParamsApiKeyNew(API_DESCRIPTION, API_KEY_ROLE_USER);
        ResponseApiKeyNew responseApiKeyNew =  syncano.apikeyNew(paramsApiKeyNew);

        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyNew.getResultCode());
        assertNotNull(responseApiKeyNew.getApiKey().getApiKey());
        assertNotNull(responseApiKeyNew.getApiKey().getId());
        assertNotNull(responseApiKeyNew.getApiKey().getType());
        assertNotNull(responseApiKeyNew.getApiKey().getRole());
        assertEquals(API_DESCRIPTION, responseApiKeyNew.getApiKey().getDescription());
        apiKey = responseApiKeyNew.getApiKey();

        // Api Key Get
        ParamsApiKeyGet paramsApiKeyGet = new ParamsApiKeyGet();
        ResponseApiKeyGet responseApiKeyGet = syncano.apikeyGet(paramsApiKeyGet);

        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyGet.getResultCode());
        assertTrue(responseApiKeyGet.getApiKey().length > 0);
        for (int i = 0; i < responseApiKeyGet.getApiKey().length ; i++) {
            assertNotNull(responseApiKeyGet.getApiKey()[i].getApiKey());
            assertNotNull(responseApiKeyGet.getApiKey()[i].getDescription());
            assertNotNull(responseApiKeyGet.getApiKey()[i].getId());
            assertNotNull(responseApiKeyGet.getApiKey()[i].getType());
            assertNotNull(responseApiKeyGet.getApiKey()[i].getRole());
        }

        // Api Key Get One
        ParamsApiKeyGetOne paramsApiKeyGetOne = new ParamsApiKeyGetOne();
        paramsApiKeyGetOne.setApiClientId(apiKey.getId());
        ResponseApiKeyGetOne responseApiKeyGetOne = syncano.apikeyGetOne(paramsApiKeyGetOne);

        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyGetOne.getResultCode());
        assertNotNull(responseApiKeyGetOne.getApiKey().getApiKey());
        assertNotNull(responseApiKeyGetOne.getApiKey().getId());
        assertNotNull(responseApiKeyGetOne.getApiKey().getType());
        assertNotNull(responseApiKeyGetOne.getApiKey().getRole());
        assertEquals(API_DESCRIPTION, responseApiKeyGetOne.getApiKey().getDescription());

        // Api Key Update Description
        ParamsApiKeyUpdateDescription paramsApiKeyUpdateDescription = new ParamsApiKeyUpdateDescription(API_DESCRIPTION_UPDATED);
        ResponseApiKeyUpdateDescription responseApiKeyUpdateDescription = syncano.apikeyUpdateDescription(paramsApiKeyUpdateDescription);
        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyUpdateDescription.getResultCode());
        assertEquals(API_DESCRIPTION_UPDATED, responseApiKeyUpdateDescription.getApiKey().getDescription());

        // Api Key Authorize
        ParamsApiKeyAuthorize paramsApiKeyAuthorize = new ParamsApiKeyAuthorize(apiKey.getId(), PERMISSION_SEND_NOTIFICATION);
        Response responseApiKeyAuthorize = syncano.apikeyAuthorize(paramsApiKeyAuthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyAuthorize.getResultCode());

        // Api Key Deauthorize
        ParamsApiKeyDeauthorize paramsApiKeyDeauthorize = new ParamsApiKeyDeauthorize(apiKey.getId(), PERMISSION_SEND_NOTIFICATION);
        Response responseApiKeyDeauthorize = syncano.apikeyDeauthorize(paramsApiKeyDeauthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyDeauthorize.getResultCode());

        // Api Key Delete
        ParamsApiKeyDelete paramsApiKeyDelete = new ParamsApiKeyDelete(apiKey.getId());
        Response responseApiKeyDelete = syncano.apikeyDelete(paramsApiKeyDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseApiKeyDelete.getResultCode());
    }
}
