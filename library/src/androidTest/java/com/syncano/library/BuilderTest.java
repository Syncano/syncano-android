package com.syncano.library;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;

import java.util.List;

public class BuilderTest extends SyncanoApplicationTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";

    public void setUp() throws Exception {
        super.setUp();

        // delete user
        deleteTestUser(syncano, USER_NAME);

        //delete class
        removeClass(Something.class);
    }


    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testBuilder() {
        Syncano userSyncano = new SyncanoBuilder().useLoggedUserStorage(false).androidContext(getContext()).
                apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME).build();

        // register user
        User newUser = new User(USER_NAME, PASSWORD);
        Response<User> response = userSyncano.registerUser(newUser).send();
        assertTrue(response.isSuccess());
        User user = response.getData();
        assertNotNull(user);

        Response<User> responseLogin = userSyncano.loginUser(USER_NAME, PASSWORD).send();
        assertTrue(responseLogin.isSuccess());

        // create new class
        SyncanoClass newClazz = new SyncanoClass(Something.class);
        Response<SyncanoClass> classResp = syncano.createSyncanoClass(newClazz).send();
        assertTrue(classResp.isSuccess());

        // create object
        Something newSom = new Something();
        newSom.someText = "some text";
        newSom.setOwnerPermissions(DataObjectPermissions.WRITE);
        newSom.setOtherPermissions(DataObjectPermissions.NONE);
        Response<Something> respCreateObj = syncano.createObject(newSom).send();
        assertTrue(respCreateObj.isSuccess());
        Something som = respCreateObj.getData();
        assertNotNull(som);

        // get object without permission
        Response<List<Something>> respGetObj = userSyncano.getObjects(Something.class).send();
        assertTrue(respGetObj.isSuccess());
        List<Something> soms = respGetObj.getData();
        assertNotNull(soms);
        assertTrue(soms.size() == 0);

        // give permission
        som.setOwner(user.getId());
        Response<Something> respUpdateObj = syncano.updateObject(som).send();
        assertTrue(respUpdateObj.isSuccess());

        // get objects
        Response<List<Something>> respGetObjWithPerm = userSyncano.getObjects(Something.class).send();
        assertTrue(respGetObjWithPerm.isSuccess());
        List<Something> soms2 = respGetObjWithPerm.getData();
        assertNotNull(soms2);
        assertTrue(soms2.size() > 0);

        // check if user configuration will survive creating new syncano object
        userSyncano = new SyncanoBuilder().useLoggedUserStorage(true).androidContext(getContext()).
                apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME).build();
        Response<List<Something>> respNewSyn = userSyncano.getObjects(Something.class).send();
        assertFalse(respNewSyn.isSuccess());
        userSyncano.setUser(newUser);

        // again check if user configuration will survive creating new syncano object
        new SyncanoBuilder().useLoggedUserStorage(true).androidContext(getContext()).
                apiKey(BuildConfig.API_KEY_USERS).instanceName(BuildConfig.INSTANCE_NAME).setAsGlobalInstance(true).build();

        respNewSyn = Syncano.getInstance().getObjects(Something.class).send();
        assertTrue(respNewSyn.isSuccess());
        soms2 = respNewSyn.getData();
        assertNotNull(respNewSyn);
        assertTrue(soms2.size() > 0);
    }

    @com.syncano.library.annotation.SyncanoClass(name = "just_something")
    private static class Something extends SyncanoObject {
        @SyncanoField(name = "some_text")
        public String someText;
    }
}
