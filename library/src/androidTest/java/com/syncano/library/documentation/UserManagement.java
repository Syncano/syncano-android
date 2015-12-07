package com.syncano.library.documentation;

import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Profile;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.User;
import com.syncano.library.utils.SyncanoClassHelper;

import java.io.File;
import java.util.List;

public class UserManagement extends SyncanoApplicationTestCase {
    private final String userName = "userlogin";
    private final String password = "userpass";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteTestUser(syncano, userName);
        User newUser = new User(userName, password);
        Response<User> response = syncano.registerUser(newUser).send();

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
        Response<User> response = newUser.register();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testUpdateUserClass() {
        // ---------- You can make same kind of changes to user_profile class
        Response<SyncanoClass> responseGetProfileClass = syncano.getSyncanoClass(Constants.USER_PROFILE_CLASS_NAME).send();
        SyncanoClass profileClass = responseGetProfileClass.getData();
        profileClass.setSchema(SyncanoClassHelper.getSyncanoClassSchema(MyUserProfile.class));
        Response<SyncanoClass> response = syncano.updateSyncanoClass(profileClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());

        deleteTestUser(syncano, userName);
        MyUser newUser = new MyUser(userName, password);
        Response<MyUser> responseRegister = newUser.register();
        assertEquals(Response.HTTP_CODE_CREATED, responseRegister.getHttpResultCode());
        MyUser user = responseRegister.getData();

        // ---------- Now a user has an extra avatar field in their user profile, which the user can update with an avatar picture
        MyUserProfile profile = user.getProfile();
        profile.avatar = new SyncanoFile(new File(getContext().getFilesDir(), "blue.png"));
        Response<MyUserProfile> responseAvatar = profile.save();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, responseAvatar.getHttpResultCode());
    }

    public void testUserAuthentication() {
        // ---------- User authentication
        Response<User> response = syncano.loginUser(userName, password).send();
        // All next requests will be used using apiKey and userKey.
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        String userKey = response.getData().getUserKey();
        assertNotNull(userKey);


        // ---------- Acting as a user
        syncano.setUserKey(userKey);

        // -----------------------------


        // ---------- How to reset the User Key
        syncano.setUserKey(null);

        // -----------------------------
    }

    public void testSocialUserAuthentication() {
        String socialNetworkAuthToken = "";

        // ---------- Social Login
        Response<User> response = syncano.loginSocialUser(SocialAuthBackend.FACEBOOK, socialNetworkAuthToken).send();

        // -----------------------------
    }

    @com.syncano.library.annotation.SyncanoClass(name = Constants.USER_PROFILE_CLASS_NAME)
    private static class MyUserProfile extends Profile {
        @SyncanoField(name = "avatar")
        public SyncanoFile avatar;
    }

    private static class MyUser extends AbstractUser<MyUserProfile> {
        public MyUser(String login, String pass) {
            super(login, pass);
        }
    }
}
