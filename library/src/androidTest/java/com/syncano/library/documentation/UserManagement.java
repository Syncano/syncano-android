package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestUserProfileClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.User;

import java.util.List;

public class UserManagement extends SyncanoApplicationTestCase {
    private final String userName = "userlogin";
    private final String password = "userpass";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTestUser(syncano, userName);
        User newUser = new User(userName, password);
        Response<User> response = syncano.createUser(newUser).send();

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        deleteTestUser(syncano, userName);
    }

    public static void deleteTestUser(Syncano syncano, String userName) {
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
        deleteTestUser(syncano, userName);

        // ---------- For adding new user in code
        User newUser = new User(userName, password);
        Response<User> response = syncano.createUser(newUser).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testUpdateUserClass() {
        JsonArray schema = TestUserProfileClass.getSchema();

        // ---------- You can make same kind of changes to user_profile class
        Response<SyncanoClass> responseGetProfileClass = syncano.getSyncanoClass(Constants.USER_PROFILE_CLASS_NAME).send();

        SyncanoClass profileClass = responseGetProfileClass.getData();
        profileClass.setSchema(schema);
        Response<SyncanoClass> response = syncano.updateSyncanoClass(profileClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());


        // ---------- Now a user has an extra avatar field in their user profile, which the user can update with an avatar picture
        // Not supported in Android library
        // -----------------------------
    }

    public void testUserAuthentication() {
        // ---------- User authentication
        Response<User> response = syncano.authUser(userName, password).send();

        // This is how user key should be used.
        // All next requests will be used using apiKey and userKey.
        syncano.setUserKey(response.getData().getUserKey());
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        String userKey = response.getData().getUserKey();
        assertNotNull(userKey);


        // ---------- Acting as a user
        syncano.setUserKey(userKey);

        // -----------------------------


        // ---------- How to reset the User Key
        // No example

        // -----------------------------
    }

    public void testSocialUserAuthentication() {
        String socialNetworkAuthToken = "";

        // ---------- Social Login
        Response<User> response = syncano.authSocialUser(SocialAuthBackend.FACEBOOK, socialNetworkAuthToken).send();

        // -----------------------------
    }
}
