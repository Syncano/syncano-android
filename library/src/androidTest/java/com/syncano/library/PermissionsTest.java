package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.Model.SomeV1;
import com.syncano.library.api.Response;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PermissionsTest extends SyncanoAndroidTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";
    private Syncano userSyncano;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // delete user
        deleteTestUser(syncano, USER_NAME);

        //delete class
        removeClass(SomeV1.class);

        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME, getContext());
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testUserPermissionOnObject() {
        // register user
        User newUser = new User(USER_NAME, PASSWORD);
        Response<User> response = userSyncano.registerUser(newUser).send();
        assertTrue(response.isSuccess());
        User user = response.getData();
        assertNotNull(user);

        Response<User> responseLogin = userSyncano.loginUser(USER_NAME, PASSWORD).send();
        assertTrue(responseLogin.isSuccess());

        // create new class
        SyncanoClass newClazz = new SyncanoClass(SomeV1.class);
        Response<SyncanoClass> classResp = syncano.createSyncanoClass(newClazz).send();
        assertTrue(classResp.isSuccess());

        // create object
        SomeV1 newSom = new SomeV1();
        newSom.someText = "some text";
        newSom.setOwnerPermissions(DataObjectPermissions.WRITE);
        newSom.setOtherPermissions(DataObjectPermissions.NONE);
        Response<SomeV1> respCreateObj = syncano.createObject(newSom).send();
        assertTrue(respCreateObj.isSuccess());
        SomeV1 som = respCreateObj.getData();
        assertNotNull(som);

        // get object without permission
        Response<List<SomeV1>> respGetObj = userSyncano.getObjects(SomeV1.class).send();
        assertTrue(respGetObj.isSuccess());
        List<SomeV1> soms = respGetObj.getData();
        assertNotNull(soms);
        assertTrue(soms.size() == 0);

        // give permission
        som.setOwner(user.getId());
        Response<SomeV1> respUpdateObj = syncano.updateObject(som).send();
        assertTrue(respUpdateObj.isSuccess());

        // get objects
        Response<List<SomeV1>> respGetObjWithPerm = userSyncano.getObjects(SomeV1.class).send();
        assertTrue(respGetObjWithPerm.isSuccess());
        List<SomeV1> soms2 = respGetObjWithPerm.getData();
        assertNotNull(soms2);
        assertTrue(soms2.size() > 0);

        // check if user configuration will survive creating new syncano object
        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME, getContext());
        Response<List<SomeV1>> respNewSyn = userSyncano.getObjects(SomeV1.class).send();
        assertTrue(respNewSyn.isSuccess());
        soms2 = respNewSyn.getData();
        assertNotNull(respNewSyn);
        assertTrue(soms2.size() > 0);

        // user configuration not available without context
        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME);
        Response<List<SomeV1>> respNoCtx = userSyncano.getObjects(SomeV1.class).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respNoCtx.getHttpResultCode());

        // check if possible to logout user
        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME, getContext());
        userSyncano.setUser(null);

        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME, getContext());
        assertNull(userSyncano.getUser());
        assertNull(userSyncano.getUserKey());
    }
}
