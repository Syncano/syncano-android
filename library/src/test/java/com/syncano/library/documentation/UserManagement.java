package com.syncano.library.documentation;

import com.syncano.library.BuildConfig;
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
import com.syncano.library.utils.SyncanoLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UserManagement extends SyncanoApplicationTestCase {
    private final String userName = "userlogin";
    private final String password = "userpass";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deleteTestUser(syncano, userName);
        User newUser = new User(userName, password);
        Response<User> response = syncano.registerUser(newUser).send();

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    @After
    public void tearDown() throws Exception {
        deleteTestUser(syncano, userName);
        super.tearDown();
    }


    @Test
    public void testCreateUser() {
        deleteTestUser(syncano, userName);

        // ---------- For adding new user in code
        User newUser = new User(userName, password);
        Response<User> response = newUser.register();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    @Test
    public void testUpdateUserClass() {
        // ---------- You can make same kind of changes to user_profile class

        // MyUserProfile should extend Profile class
        Response<SyncanoClass> response = syncano.updateSyncanoClass(MyUserProfile.class).send();

        // -----------------------------

        assertTrue(response.isSuccess());

        deleteTestUser(syncano, userName);
        MyUser newUser = new MyUser(userName, password);
        Response<MyUser> responseRegister = newUser.register();
        assertEquals(Response.HTTP_CODE_CREATED, responseRegister.getHttpResultCode());
        MyUser user = responseRegister.getData();

        // ---------- Now a user has an extra avatar field in their user profile, which the user can update with an avatar picture
        MyUserProfile profile = user.getProfile();
        profile.avatar = new SyncanoFile(new File(getAssetsDir(), "blue.png"));
        Response<MyUserProfile> responseAvatar = profile.save();
        // -----------------------------

        assertTrue(responseAvatar.isSuccess());
    }

    @Test
    public void testUserAuthentication() {
        //syncano instance with acl enabled
        Syncano userSyncano = new Syncano(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME);
        // ---------- User authentication
        User plainUser = new User(userName, password);

        Response<User> response = plainUser.on(userSyncano).login();
        // All next requests will be used using apiKey and userKey.
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
        User user = response.getData();
        assertNotNull(user);
        // ---------- Acting as a user

        // Don't have to do it if used login method on this Syncano object
        userSyncano.setUser(user);
        Response<User> fetchUserResponse = user.on(userSyncano).fetch();
        assertTrue(fetchUserResponse.isSuccess());
        Response<Profile> fetchUserProfile = user.on(userSyncano).fetchProfile();
        assertTrue(fetchUserProfile.isSuccess());


        // -----------------------------


        // ---------- How to reset the User Key
        userSyncano.setUser(null);

        // -----------------------------
    }

    @Test
    public void testFacebookSocialUserAuthentication() {
        String socialNetworkAuthToken = BuildConfig.FACEBOOK_TOKEN;
        if (tokenIsInvalid(socialNetworkAuthToken)) {
            SyncanoLog.d(getClass().getSimpleName(), "Facebook token missed, test will be skipped");
            return;
        }
        Response<User> userResponse = new User(SocialAuthBackend.FACEBOOK, socialNetworkAuthToken).loginSocialUser();
        assertTrue(userResponse.isSuccess());
        Response<User> syncanoResponse = syncano.loginSocialUser(SocialAuthBackend.FACEBOOK, socialNetworkAuthToken).send();
        assertTrue(syncanoResponse.isSuccess());
    }

    @Test
    public void testGoogleSocialUserAuthentication() {
        String socialNetworkAuthToken = BuildConfig.GOOGLE_TOKEN;
        if (tokenIsInvalid(socialNetworkAuthToken)) {
            SyncanoLog.d(getClass().getSimpleName(), "Google token missed, test will be skipped");
            return;
        }
        Response<User> userResponse = new User(SocialAuthBackend.GOOGLE_OAUTH2, socialNetworkAuthToken).loginSocialUser();
        assertTrue(userResponse.isSuccess());
        Response<User> syncanoResponse = syncano.loginSocialUser(SocialAuthBackend.GOOGLE_OAUTH2, socialNetworkAuthToken).send();
        assertTrue(syncanoResponse.isSuccess());
    }

    @Test
    public void testLinkedInSocialUserAuthentication() {
        String socialNetworkAuthToken = BuildConfig.LINKEDIN_TOKEN;
        if (tokenIsInvalid(socialNetworkAuthToken)) {
            SyncanoLog.d(getClass().getSimpleName(), "Linkedin token missed, test will be skipped");
            return;
        }
        Response<User> userResponse = new User(SocialAuthBackend.LINKEDIN, socialNetworkAuthToken).loginSocialUser();
        assertTrue(userResponse.isSuccess());
        Response<User> syncanoResponse = syncano.loginSocialUser(SocialAuthBackend.LINKEDIN, socialNetworkAuthToken).send();
        assertTrue(syncanoResponse.isSuccess());
    }

    @Test
    public void testTwitterSocialUserAuthentication() {
        String socialNetworkAuthToken = BuildConfig.TWITTER_TOKEN;
        if (tokenIsInvalid(socialNetworkAuthToken)) {
            SyncanoLog.d(getClass().getSimpleName(), "Twitter token missed, test will be skipped");
            return;
        }
        Response<User> userResponse = new User(SocialAuthBackend.TWITTER, socialNetworkAuthToken).loginSocialUser();
        assertTrue(userResponse.isSuccess());
        Response<User> syncanoResponse = syncano.loginSocialUser(SocialAuthBackend.TWITTER, socialNetworkAuthToken).send();
        assertTrue(syncanoResponse.isSuccess());
    }


    private boolean tokenIsInvalid(String token) {
        return token == null || token.length() == 0;
    }

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
