package com.syncano.android.test.modules.users;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.data.ParamsDataDelete;
import com.syncano.android.lib.modules.data.ParamsDataNew;
import com.syncano.android.lib.modules.data.ResponseDataNew;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.modules.users.ParamsUserCount;
import com.syncano.android.lib.modules.users.ParamsUserDelete;
import com.syncano.android.lib.modules.users.ParamsUserGet;
import com.syncano.android.lib.modules.users.ParamsUserGetAll;
import com.syncano.android.lib.modules.users.ParamsUserGetOne;
import com.syncano.android.lib.modules.users.ParamsUserLogin;
import com.syncano.android.lib.modules.users.ParamsUserNew;
import com.syncano.android.lib.modules.users.ParamsUserUpdate;
import com.syncano.android.lib.modules.users.ResponseUserCount;
import com.syncano.android.lib.modules.users.ResponseUserGet;
import com.syncano.android.lib.modules.users.ResponseUserGetAll;
import com.syncano.android.lib.modules.users.ResponseUserGetOne;
import com.syncano.android.lib.modules.users.ResponseUserLogin;
import com.syncano.android.lib.modules.users.ResponseUserNew;
import com.syncano.android.lib.modules.users.ResponseUserUpdate;
import com.syncano.android.lib.objects.Data;
import com.syncano.android.lib.objects.User;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class UsersTest extends AndroidTestCase {

    private static final String USER_NAME = "SyncanoAndroidTestUser";
    private static final String USER_NICK = "NickName";
    private static final String USER_UPDATED_NICK = "NickNameUpdated";
    private static final String USER_PASSWORD = "password";
    private static final String USER_NEW_PASSWORD = "new_password";

    private Syncano syncano = null;
    private String projectId = null;
    private String collectionId = null;

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

        // Collection New
        ParamsCollectionNew paramsCollectionNew = new ParamsCollectionNew(projectId, "CI Test Collection");
        ResponseCollectionNew responseCollectionNew = syncano.collectionNew(paramsCollectionNew);
        assertEquals("Failed to create new collection", Response.CODE_SUCCESS, (int) responseCollectionNew.getResultCode());
        collectionId = responseCollectionNew.getCollection().getId();

        // User with test login may already exist. Should be deleted before tests.
        // Don't assert result. If there is no user it will fail.
        ParamsUserDelete paramsUserDelete = new ParamsUserDelete(null, USER_NAME);
        syncano.userDelete(paramsUserDelete);
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
    }

    public void testUsers() {

        User user;

        // User New
        ParamsUserNew paramsUserNew = new ParamsUserNew(USER_NAME);
        paramsUserNew.setPassword(USER_PASSWORD);
        paramsUserNew.setNick(USER_NICK);
        ResponseUserNew responseUserNew = syncano.userNew(paramsUserNew);

        assertEquals(Response.CODE_SUCCESS, (int)responseUserNew.getResultCode());
        assertNotNull(responseUserNew.getUser().getId());
        assertEquals(USER_NAME, responseUserNew.getUser().getName());
        assertEquals(USER_NICK, responseUserNew.getUser().getNick());
        user = responseUserNew.getUser();

        // User Login
        ParamsUserLogin paramsUserLogin = new ParamsUserLogin(USER_NAME, USER_PASSWORD);
        ResponseUserLogin responseUserLogin = syncano.userLogin(paramsUserLogin);
        assertEquals(Response.CODE_SUCCESS, (int)responseUserLogin.getResultCode());
        assertNotNull(responseUserLogin.getAuthKey());

        // User Get All
        ParamsUserGetAll paramsUserGetAll = new ParamsUserGetAll();
        ResponseUserGetAll responseUserGetAll = syncano.userGetAll(paramsUserGetAll);

        assertEquals(Response.CODE_SUCCESS, (int)responseUserGetAll.getResultCode());
        assertTrue(responseUserGetAll.getUser().length > 0);
        for (int i = 0; i < responseUserGetAll.getUser().length ; i++) {
            assertNotNull(responseUserGetAll.getUser()[i].getId());
            assertNotNull(responseUserGetAll.getUser()[i].getName());
        }

        // User Get
        ParamsDataNew paramsDataNew = new ParamsDataNew(projectId, collectionId, null, Data.PENDING);
        paramsDataNew.setUserName(user.getName());
        ResponseDataNew responseDataNew =  syncano.dataNew(paramsDataNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataNew.getResultCode());

        ParamsUserGet paramsUserGet = new ParamsUserGet(projectId, collectionId, null);
        ResponseUserGet responseUserGet = syncano.userGet(paramsUserGet);

        assertEquals(Response.CODE_SUCCESS, (int)responseUserGet.getResultCode());
        assertTrue(responseUserGet.getUser().length > 0);
        for (int i = 0; i < responseUserGet.getUser().length ; i++) {
            assertNotNull(responseUserGet.getUser()[i].getId());
            assertNotNull(responseUserGet.getUser()[i].getName());
        }

        ParamsDataDelete paramsDataDelete = new ParamsDataDelete(projectId, collectionId, null);
        paramsDataDelete.setDataIds(new String[] {responseDataNew.getData().getId()});
        Response responseDataDelete = syncano.dataDelete(paramsDataDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataDelete.getResultCode());

        // User Get One
        ParamsUserGetOne paramsUserGetOne = new ParamsUserGetOne(user.getId(), null);
        ResponseUserGetOne responseUserGetOne = syncano.userGetOne(paramsUserGetOne);

        assertEquals(Response.CODE_SUCCESS, (int) responseUserGetOne.getResultCode());
        assertNotNull(responseUserGetOne.getUser().getId());
        assertEquals(USER_NAME, responseUserGetOne.getUser().getName());
        assertEquals(USER_NICK, responseUserGetOne.getUser().getNick());

        // User Update
        ParamsUserUpdate paramsUserUpdate = new ParamsUserUpdate(user.getId(), null);
        paramsUserUpdate.setNewPassword(USER_NEW_PASSWORD);
        paramsUserUpdate.setNick(USER_UPDATED_NICK);
        ResponseUserUpdate responseUserUpdate = syncano.userUpdate(paramsUserUpdate);
        assertEquals(Response.CODE_SUCCESS, (int) responseUserUpdate.getResultCode());
        assertEquals(USER_UPDATED_NICK, responseUserUpdate.getUser().getNick());

        // User Count
        ParamsUserCount paramsUserCount = new ParamsUserCount();
        ResponseUserCount responseUserCount = syncano.userCount(paramsUserCount);
        assertEquals(Response.CODE_SUCCESS, (int)responseUserCount.getResultCode());
        assertTrue(responseUserCount.getCount() > 0);

        // User Delete
        ParamsUserDelete paramsUserDelete = new ParamsUserDelete(user.getId(), null);
        Response responseUserDelete = syncano.userDelete(paramsUserDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseUserDelete.getResultCode());
    }
}