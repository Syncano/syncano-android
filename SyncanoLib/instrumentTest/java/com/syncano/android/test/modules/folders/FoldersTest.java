package com.syncano.android.test.modules.folders;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyDelete;
import com.syncano.android.lib.modules.apikeys.ParamsApiKeyNew;
import com.syncano.android.lib.modules.apikeys.ResponseApiKeyNew;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.folders.ParamsFolderAuthorize;
import com.syncano.android.lib.modules.folders.ParamsFolderDeauthorize;
import com.syncano.android.lib.modules.folders.ParamsFolderDelete;
import com.syncano.android.lib.modules.folders.ParamsFolderGet;
import com.syncano.android.lib.modules.folders.ParamsFolderGetOne;
import com.syncano.android.lib.modules.folders.ParamsFolderNew;
import com.syncano.android.lib.modules.folders.ParamsFolderUpdate;
import com.syncano.android.lib.modules.folders.ResponseFolderGet;
import com.syncano.android.lib.modules.folders.ResponseFolderGetOne;
import com.syncano.android.lib.modules.folders.ResponseFolderNew;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.objects.ApiKey;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class FoldersTest extends AndroidTestCase {

    private static final String API_KEY_ROLE_USER = "user";
    private static final String FOLDER_NAME = "Test Folder";
    private static final String NEW_FOLDER_NAME = "Test Folder New";
    private static final String PERMISSION_READ_DATA = "read_data";

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

        // Collection New
        ParamsCollectionNew paramsCollectionNew = new ParamsCollectionNew(projectId, "CI Test Collection");
        ResponseCollectionNew responseCollectionNew = syncano.collectionNew(paramsCollectionNew);
        assertEquals("Failed to create new collection", Response.CODE_SUCCESS, (int) responseCollectionNew.getResultCode());
        collectionId = responseCollectionNew.getCollection().getId();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Collection Delete
        ParamsCollectionDelete paramsCollectionDelete = new ParamsCollectionDelete(projectId, collectionId);
        Response responseCollectionDelete = syncano.collectionDelete(paramsCollectionDelete);
        assertEquals("Failed to clean test collection", Response.CODE_SUCCESS, (int)responseCollectionDelete.getResultCode());

        // Clean test project
        ParamsProjectDelete paramsProjectDelete = new ParamsProjectDelete(projectId);
        Response responseProjectDelete = syncano.projectDelete(paramsProjectDelete);
        assertEquals("Failed to clean test project", Response.CODE_SUCCESS, (int) responseProjectDelete.getResultCode());

        // Clean Api Key
        ParamsApiKeyDelete paramsApiKeyDelete = new ParamsApiKeyDelete(apiKey.getId());
        Response responseApiKeyDelete = syncano.apikeyDelete(paramsApiKeyDelete);
        assertEquals("Failed to clean  Api Key", Response.CODE_SUCCESS, (int) responseApiKeyDelete.getResultCode());

    }

    public void testFolders() {

        // Folder New
        ParamsFolderNew paramsFolderNew = new ParamsFolderNew(projectId, collectionId, null, FOLDER_NAME);
        ResponseFolderNew responseFolderNew = syncano.folderNew(paramsFolderNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderNew.getResultCode());

        // Folder Get
        ParamsFolderGet paramsFolderGet = new ParamsFolderGet(projectId, collectionId, null);
        ResponseFolderGet responseFolderGet = syncano.folderGet(paramsFolderGet);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderGet.getResultCode());
        assertTrue(responseFolderGet.getFolder().length > 0);

        // Folder Get One
        ParamsFolderGetOne paramsFolderGetOne = new ParamsFolderGetOne(projectId, collectionId, null, FOLDER_NAME);
        ResponseFolderGetOne responseFolderGetOne = syncano.folderGetOne(paramsFolderGetOne);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderGetOne.getResultCode());

        // Folder Update
        ParamsFolderUpdate paramsFolderUpdate = new ParamsFolderUpdate(projectId, collectionId, null, FOLDER_NAME);
        paramsFolderUpdate.setNewName(NEW_FOLDER_NAME); // NEW_FOLDER_NAME instead of FOLDER_NAME should be used from now.
        Response responseFolderUpdate = syncano.folderUpdate(paramsFolderUpdate);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderUpdate.getResultCode());

        // Folder Authorize
        ParamsFolderAuthorize paramsFolderAuthorize = new ParamsFolderAuthorize(apiKey.getId(), PERMISSION_READ_DATA, projectId, collectionId, null, NEW_FOLDER_NAME);
        Response responseFolderAuthorize = syncano.folderAuthorize(paramsFolderAuthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderAuthorize.getResultCode());

        // Folder Deauthorize
        ParamsFolderDeauthorize paramsFolderDeauthorize = new ParamsFolderDeauthorize(apiKey.getId(), PERMISSION_READ_DATA, projectId, collectionId, null, NEW_FOLDER_NAME);
        Response responseFolderDeauthorize = syncano.folderDeauthorize(paramsFolderDeauthorize);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderDeauthorize.getResultCode());

        // Folder Delete
        ParamsFolderDelete paramsFolderDelete = new ParamsFolderDelete(projectId, collectionId, null, NEW_FOLDER_NAME);
        Response responseFolderDelete = syncano.folderDelete(paramsFolderDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseFolderDelete.getResultCode());
    }
}