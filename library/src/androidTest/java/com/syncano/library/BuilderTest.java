package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.Model.SomeV1;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BuilderTest extends SyncanoAndroidTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // delete user
        deleteTestUser(syncano, USER_NAME);

        //delete class
        removeClass(SomeV1.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testBuilder() {
        Syncano userSyncano = new SyncanoBuilder().useLoggedUserStorage(false).androidContext(getContext()).
                apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME)
                .customServerUrl(BuildConfig.STAGING_SERVER_URL).build();

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
        ResponseGetList<SomeV1> respGetObj = userSyncano.getObjects(SomeV1.class).send();
        assertTrue(respGetObj.isSuccess());
        List<SomeV1> soms = respGetObj.getData();
        assertNotNull(soms);
        assertTrue(soms.size() == 0);

        // give permission
        som.setOwner(user.getId());
        Response<SomeV1> respUpdateObj = syncano.updateObject(som).send();
        assertTrue(respUpdateObj.isSuccess());

        // get objects
        ResponseGetList<SomeV1> respGetObjWithPerm = userSyncano.getObjects(SomeV1.class).send();
        assertTrue(respGetObjWithPerm.isSuccess());
        List<SomeV1> soms2 = respGetObjWithPerm.getData();
        assertNotNull(soms2);
        assertTrue(soms2.size() > 0);

        // check if user configuration will survive creating new syncano object
        userSyncano = new SyncanoBuilder().useLoggedUserStorage(true).androidContext(getContext()).
                apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME)
                .customServerUrl(BuildConfig.STAGING_SERVER_URL).build();
        ResponseGetList<SomeV1> respNewSyn = userSyncano.getObjects(SomeV1.class).send();
        assertFalse(respNewSyn.isSuccess());
        userSyncano.setUser(newUser);

        // again check if user configuration will survive creating new syncano object
        new SyncanoBuilder().useLoggedUserStorage(true).androidContext(getContext()).
                apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME)
                .customServerUrl(BuildConfig.STAGING_SERVER_URL).setAsGlobalInstance(true).build();

        respNewSyn = Syncano.getInstance().getObjects(SomeV1.class).send();
        assertTrue(respNewSyn.isSuccess());
        soms2 = respNewSyn.getData();
        assertNotNull(respNewSyn);
        assertTrue(soms2.size() > 0);
    }
}
