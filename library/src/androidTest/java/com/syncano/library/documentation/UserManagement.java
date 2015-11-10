package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.syncano.library.Constants;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestUserProfileClass;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.User;

import java.util.List;

//TODO finish this tomorrow
public class UserManagement extends SyncanoApplicationTestCase {
    private final String userName = "userlogin";
    private final String password = "userpass";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTestUser();
        User newUser = new User(userName, password);
        Response<User> response = syncano.createUser(newUser).send();

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteTestUser();
    }

    private void deleteTestUser() {
        Response<List<User>> response = syncano.getUsers().send();

        if (response.getData() != null && response.getData().size() > 0) {
            for (User u : response.getData()) {
                if (userName.equals(u.getUserName())) {
                    syncano.deleteUser(u.getId()).send();
                }
            }
        }
    }

    public void testCreateUser() {
        deleteTestUser();

        // ---------- For adding new user in code,
        User newUser = new User(userName, password);
        Response<User> response = syncano.createUser(newUser).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testUpdateUserClass() {
        JsonArray schema = TestUserProfileClass.getSchema();

        // ---------- For adding new user in code,
        Response<SyncanoClass> responseGetProfileClass = syncano.getSyncanoClass(Constants.USER_PROFILE_CLASS_NAME).send();

        SyncanoClass profileClass = responseGetProfileClass.getData();
        profileClass.setSchema(schema);
        Response<SyncanoClass> response = syncano.updateSyncanoClass(profileClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }

    public void testUpdateAvatar() {
        // TODO This example has to be written after added support for files
    }
}
