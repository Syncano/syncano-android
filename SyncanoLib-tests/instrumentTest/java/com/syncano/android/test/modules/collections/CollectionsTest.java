package com.syncano.android.test.modules.collections;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDelete;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyNew;
import com.syncano.android.lib.modules.collections.ParamsCollectionActivate;
import com.syncano.android.lib.modules.collections.ParamsCollectionAddTag;
import com.syncano.android.lib.modules.collections.ParamsCollectionAuthorize;
import com.syncano.android.lib.modules.collections.ParamsCollectionDeactivate;
import com.syncano.android.lib.modules.collections.ParamsCollectionDeauthorize;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionDeleteTag;
import com.syncano.android.lib.modules.collections.ParamsCollectionGet;
import com.syncano.android.lib.modules.collections.ParamsCollectionGetOne;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ParamsCollectionUpdate;
import com.syncano.android.lib.modules.collections.ResponseCollectionGet;
import com.syncano.android.lib.modules.collections.ResponseCollectionGetOne;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionUpdate;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.objects.ApiKey;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.Config.Constants;

public class CollectionsTest extends AndroidTestCase {

    private static final String API_KEY_ROLE_USER = "user";
    private static final String COLLECTION_DESCRIPTION_UPDATED = "Updated Description";
    private static final String PERMISSION_READ_DATA = "read_data";
    private static final String COLLECTION_TAG = "news";

    private Syncano syncano = null;
    private String projectId = null;
    private String collectionId = null;
    private ApiKey apiKey = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));

        // Create test project
        ParamsProjectNew paramsProjectNew = new ParamsProjectNew("CI Test Project");
        ResponseProjectNew responseProjectNew = syncano.projectNew(paramsProjectNew);
        assertEquals("Failed to create test project", Response.CODE_SUCCESS, (int) responseProjectNew.getResultCode());
        projectId = responseProjectNew.getProject().getId();

        // Create API Key
        ParamsApiKeyNew paramsApiKeyNew = new ParamsApiKeyNew("test description", API_KEY_ROLE_USER);
        ResponseApiKeyNew responseApiKeyNew =  syncano.apikeyNew(paramsApiKeyNew);
        assertEquals("Failed to create new Api Key", Response.CODE_SUCCESS, (int) responseApiKeyNew.getResultCode());
        apiKey = responseApiKeyNew.getApiKey();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Clean test project
        ParamsProjectDelete paramsProjectDelete = new ParamsProjectDelete(projectId);
        Response responseProjectDelete = syncano.projectDelete(paramsProjectDelete);
        assertEquals("Failed to clean test project", Response.CODE_SUCCESS, (int) responseProjectDelete.getResultCode());

        // Clean Api Key
        ParamsApiKeyDelete paramsApiKeyDelete = new ParamsApiKeyDelete(apiKey.getId());
        Response responseApiKeyDelete = syncano.apikeyDelete(paramsApiKeyDelete);
        assertEquals("Failed to clean  Api Key", Response.CODE_SUCCESS, (int) responseApiKeyDelete.getResultCode());

    }

    public void testCollections() {

        // Collection New
        ParamsCollectionNew paramsCollectionNew = new ParamsCollectionNew(projectId, "CI Test Collection");
        ResponseCollectionNew responseCollectionNew = syncano.collectionNew(paramsCollectionNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionNew.getResultCode());
        collectionId = responseCollectionNew.getCollection().getId();

        // Collection Get
        ParamsCollectionGet paramsCollectionGet = new ParamsCollectionGet(projectId);
        ResponseCollectionGet responseCollectionGet = syncano.collectionGet(paramsCollectionGet);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionGet.getResultCode());
        assertTrue(responseCollectionGet.getCollection().length > 0);

        // Collection Get One
        ParamsCollectionGetOne paramsCollectionGetOne = new ParamsCollectionGetOne(projectId, collectionId);
        ResponseCollectionGetOne responseCollectionGetOne = syncano.collectionGetOne(paramsCollectionGetOne);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionGetOne.getResultCode());

        // Collection Activate
        ParamsCollectionActivate paramsCollectionActivate = new ParamsCollectionActivate(projectId, collectionId);
        Response responseCollectionActivate = syncano.collectionActivate(paramsCollectionActivate);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionActivate.getResultCode());

        // Collection Deactivate
        ParamsCollectionDeactivate paramsCollectionDeactivate = new ParamsCollectionDeactivate(projectId, collectionId);
        Response responseCollectionDeactivate = syncano.collectionDeactivate(paramsCollectionDeactivate);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionDeactivate.getResultCode());

        // Collection Update
        ParamsCollectionUpdate paramsCollectionUpdate = new ParamsCollectionUpdate(projectId, collectionId);
        paramsCollectionUpdate.setDescription(COLLECTION_DESCRIPTION_UPDATED);
        ResponseCollectionUpdate responseCollectionUpdate = syncano.collectionUpdate(paramsCollectionUpdate);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionUpdate.getResultCode());
        assertEquals(COLLECTION_DESCRIPTION_UPDATED, responseCollectionUpdate.getCollection().getDescription());

        // Collection Authorize
        ParamsCollectionAuthorize paramsCollectionAuthorize = new ParamsCollectionAuthorize(apiKey.getId(), PERMISSION_READ_DATA, projectId, collectionId, null);
        Response responseCollectionAuthorize = syncano.collectionAuthorize(paramsCollectionAuthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionAuthorize.getResultCode());

        // Collection Deauthorize
        ParamsCollectionDeauthorize paramsCollectionDeauthorize = new ParamsCollectionDeauthorize(apiKey.getId(), PERMISSION_READ_DATA, projectId, collectionId, null);
        Response responseCollectionDeauthorize = syncano.collectionDeauthorize(paramsCollectionDeauthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionDeauthorize.getResultCode());

        // Collection Add Tag
        ParamsCollectionAddTag paramsCollectionAddTag = new ParamsCollectionAddTag(projectId, collectionId, null, new String[] {COLLECTION_TAG});
        Response responseCollectionAddTag = syncano.collectionAddTag(paramsCollectionAddTag);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionAddTag.getResultCode());

        // Collection Delete Tag
        ParamsCollectionDeleteTag paramsCollectionDeleteTag = new ParamsCollectionDeleteTag(projectId, collectionId, null, new String[] {COLLECTION_TAG});
        Response responseCollectionDeleteTag = syncano.collectionDeleteTag(paramsCollectionDeleteTag);
        assertEquals(Response.CODE_SUCCESS, (int) responseCollectionDeleteTag.getResultCode());

        // Collection Delete
        ParamsCollectionDelete paramsCollectionDelete = new ParamsCollectionDelete(projectId, collectionId);
        Response responseCollectionDelete = syncano.collectionDelete(paramsCollectionDelete);
        assertEquals(Response.CODE_SUCCESS, (int)responseCollectionDelete.getResultCode());
    }
}