package com.syncano.library.tests;

import com.syncano.library.BuildConfig;
import com.syncano.library.Constants;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestUser;
import com.syncano.library.TestUserProfileObject;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SocialAuthBackend;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Profile;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.User;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class UsersTest extends SyncanoApplicationTestCase {

    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String NEW_PASSWORD = "new_password";


    @Before
    public void setUp() throws Exception {
        super.setUp();
        Response<User> responseUserAuth = syncano.loginUser(USER_NAME, PASSWORD).send();

        if (responseUserAuth.isSuccess() && responseUserAuth.getData() != null) {
            // Make sure there is no test user
            syncano.deleteUser(responseUserAuth.getData().getId()).send();
        }

        // If test failed after password change
        Response<User> responseUserNewAuth = syncano.loginUser(USER_NAME, NEW_PASSWORD).send();

        if (responseUserNewAuth.isSuccess() && responseUserNewAuth.getData() != null) {
            // Make sure there is no test user
            syncano.deleteUser(responseUserNewAuth.getData().getId()).send();
        }
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testUsers() throws InterruptedException {
        User newUser = new User(USER_NAME, PASSWORD);
        User user;

        // ----------------- Create -----------------
        Response<User> responseCreateUser = syncano.registerUser(newUser).send();

        assertTrue(responseCreateUser.isSuccess());
        assertNotNull(responseCreateUser.getData());
        user = responseCreateUser.getData();

        // ----------------- Get Profile -----------------
        assertNotNull(user.getProfile());
        Response<Profile> responseGetProfile = syncano.getObject(Profile.class, user.getProfile().getId()).send();

        assertTrue(responseGetProfile.isSuccess());
        assertNotNull(responseGetProfile.getData());

        // ----------------- Get One -----------------
        Response<User> responseGetUser = syncano.getUser(user.getId()).send();

        assertTrue(responseGetUser.isSuccess());
        assertNotNull(responseGetUser.getData());
        assertEquals(user.getUserName(), responseGetUser.getData().getUserName());

        // ----------------- Get List -----------------
        Response<List<User>> responseGetUsers = syncano.getUsers().send();

        assertNotNull(responseGetUsers.getData());
        assertTrue("List should contain at least one item.", responseGetUsers.getData().size() > 0);

        // ----------------- Update -----------------
        user.setPassword(NEW_PASSWORD);
        Response<User> responseUpdateUser = syncano.updateUser(user).send();

        assertTrue(responseUpdateUser.isSuccess());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(user.getUserName(), responseUpdateUser.getData().getUserName());

        // ----------------- User Auth -----------------
        Response<User> responseUserAuth = syncano.loginUser(USER_NAME, NEW_PASSWORD).send();

        assertTrue(responseUserAuth.isSuccess());
        assertNotNull(responseUserAuth.getData());
        assertNotNull(responseUserAuth.getData().getUserKey());

        // ----------------- Delete -----------------
        Response<User> responseDeleteUser = syncano.deleteUser(user.getId()).send();

        assertTrue(responseDeleteUser.isSuccess());
    }

    @Test
    public void testCustomUsers() throws InterruptedException {

        // ----------------- Get Profile Class -----------------
        Response<SyncanoClass> responseGetProfileClass = syncano.getSyncanoClass(Constants.USER_PROFILE_CLASS_NAME).send();

        assertTrue(responseGetProfileClass.isSuccess());
        assertNotNull(responseGetProfileClass.getData());

        // ----------------- Update Profile Class -----------------
        Response<SyncanoClass> responseUpdateProfileClass = syncano.updateSyncanoClass(TestUserProfileObject.class).send();

        assertTrue(responseUpdateProfileClass.isSuccess());
        SyncanoClass returnedClass = responseUpdateProfileClass.getData();
        assertNotNull(returnedClass);
        assertEquals(SyncanoClassHelper.getSyncanoClassSchema(TestUserProfileObject.class), returnedClass.getSchema());

        TestUser newUser = new TestUser(USER_NAME, PASSWORD);
        TestUser user;
        TestUserProfileObject testUserProfile;

        // ----------------- Create -----------------
        Response<TestUser> responseCreateUser = syncano.registerUser(newUser).send();

        assertTrue(responseCreateUser.isSuccess());
        assertNotNull(responseCreateUser.getData());
        user = responseCreateUser.getData();
        testUserProfile = responseCreateUser.getData().getProfile();

        // ----------------- Get Profile -----------------
        assertNotNull(user.getProfile());
        Response<TestUserProfileObject> responseGetProfile = syncano.getObject(TestUserProfileObject.class, user.getProfile().getId()).send();

        assertTrue(responseGetProfile.isSuccess());
        assertNotNull(responseGetProfile.getData());

        // ----------------- Update Profile -----------------
        testUserProfile.valueOne = "one";
        testUserProfile.valueTwo = "two";
        Response<TestUserProfileObject> responseUpdateProfile = syncano.updateObject(testUserProfile).send();

        assertTrue(responseUpdateProfile.isSuccess());
        TestUserProfileObject returnedUserProfile = responseUpdateProfile.getData();
        assertNotNull(returnedUserProfile);
        assertEquals(returnedUserProfile.valueOne, testUserProfile.valueOne);
        assertEquals(returnedUserProfile.valueTwo, testUserProfile.valueTwo);

        // ----------------- Get One -----------------
        Response<TestUser> responseGetUser = syncano.getUser(TestUser.class, user.getId()).send();

        assertTrue(responseGetUser.isSuccess());
        assertNotNull(responseGetUser.getData());
        assertNotNull(responseGetUser.getData().getProfile());
        assertEquals(testUserProfile.valueOne, responseGetUser.getData().getProfile().valueOne);
        assertEquals(testUserProfile.valueTwo, responseGetUser.getData().getProfile().valueTwo);

        // ----------------- Get List -----------------
        Response<List<TestUser>> responseGetUsers = syncano.getUsers(TestUser.class).send();

        assertNotNull(responseGetUsers.getData());
        assertTrue("List should contain at least one item.", responseGetUsers.getData().size() > 0);

        // ----------------- User Auth -----------------
        Response<TestUser> responseUserAuth = syncano.loginUser(TestUser.class, USER_NAME, PASSWORD).send();

        assertTrue(responseUserAuth.isSuccess());
        assertNotNull(responseUserAuth.getData());
        assertNotNull(responseUserAuth.getData().getUserKey());

        // ----------------- Update -----------------
        user.setPassword(NEW_PASSWORD);
        Response<TestUser> responseUpdateUser = syncano.updateCustomUser(user).send();

        assertTrue(responseUpdateUser.isSuccess());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(user.getUserName(), responseUpdateUser.getData().getUserName());

        // ----------------- User Auth new password -----------------
        Response<TestUser> responseUserAuthNewPass = syncano.loginUser(TestUser.class, USER_NAME, NEW_PASSWORD).send();

        assertTrue(responseUserAuthNewPass.isSuccess());
        assertNotNull(responseUserAuthNewPass.getData());
        assertNotNull(responseUserAuthNewPass.getData().getUserKey());

        // ----------------- Delete -----------------
        Response<TestUser> responseDeleteUser = syncano.deleteUser(TestUser.class, user.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteUser.getHttpResultCode());
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

    @Test
    public void testInnerClassNoEmptyConstructorProfileAndUser() {
        assertTrue(syncano.updateSyncanoClass(InnerProfile.class).send().isSuccess());

        InnerUser newUser = new InnerUser(USER_NAME, PASSWORD);
        assertTrue(newUser.register().isSuccess());

        String newVal = "lalalala";
        newUser.getProfile().valueOne = newVal;
        assertTrue(newUser.getProfile().save().isSuccess());
        assertEquals(newVal, newUser.getProfile().valueOne);
    }

    public class InnerUser extends AbstractUser<InnerProfile> {
        public InnerUser(String userName, String password) {
            super(userName, password);
        }
    }

    public class InnerProfile extends Profile {
        @SyncanoField(name = "value_one")
        public String valueOne;
    }

}