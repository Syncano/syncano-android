package com.syncano.library;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;

import java.util.List;

public class PermissionsTest extends SyncanoApplicationTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";
    private Syncano userSyncano;

    public void setUp() throws Exception {
        super.setUp();

        // delete user
        Response<List<User>> usersResponse = syncano.getUsers().send();
        List<User> users = usersResponse.getData();
        assertNotNull(users);
        for (User u : users) {
            if (USER_NAME.equals(u.getUserName())) {
                syncano.deleteUser(u.getId()).send();
                break;
            }
        }

        //delete class
        removeClass(Something.class);

        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME, getContext());
    }


    public void tearDown() throws Exception {
        super.tearDown();
    }

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
        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME, getContext());
        Response<List<Something>> respNewSyn = userSyncano.getObjects(Something.class).send();
        assertTrue(respNewSyn.isSuccess());
        soms2 = respNewSyn.getData();
        assertNotNull(respNewSyn);
        assertTrue(soms2.size() > 0);

        // user configuration not available without context
        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME);
        Response<List<Something>> respNoCtx = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respNoCtx.getHttpResultCode());
    }

    @com.syncano.library.annotation.SyncanoClass(name = "just_something")
    private static class Something extends SyncanoObject {
        @SyncanoField(name = "some_text")
        public String someText;
    }
}
