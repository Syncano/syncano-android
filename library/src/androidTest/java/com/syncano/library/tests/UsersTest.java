package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.Config;
import com.syncano.library.Constants;
import com.syncano.library.Syncano;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.TestUserClass;
import com.syncano.library.TestUserProfileClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.RuntimeName;
import com.syncano.library.data.CodeBox;
import com.syncano.library.data.Profile;
import com.syncano.library.data.RunCodeBoxResult;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.User;
import com.syncano.library.data.Webhook;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class UsersTest extends ApplicationTestCase<Application> {

    private static final String TAG = UsersTest.class.getSimpleName();

    private Syncano syncano;

    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String NEW_PASSWORD = "new_password";

    public UsersTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);

        Response<User> responseUserAuth = syncano.authUser(USER_NAME, PASSWORD).send();

        if (responseUserAuth.isOk() && responseUserAuth.getData() != null)
        {
            // Make sure there is no test user
            syncano.deleteUser(responseUserAuth.getData().getId()).send();
        }

        // If test failed after password change
        Response<User> responseUserNewAuth = syncano.authUser(USER_NAME, NEW_PASSWORD).send();

        if (responseUserNewAuth.isOk() && responseUserNewAuth.getData() != null)
        {
            // Make sure there is no test user
            syncano.deleteUser(responseUserNewAuth.getData().getId()).send();
        }
    }

    @Override
    protected void tearDown() throws Exception {
       super.tearDown();
    }


    public void testUsers() throws InterruptedException {

        final User newUser = new User(USER_NAME, PASSWORD);
        User user;

        // ----------------- Create -----------------
        Response <User> responseCreateUser = syncano.createUser(newUser).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateUser.getHttpResultCode());
        assertNotNull(responseCreateUser.getData());
        user = responseCreateUser.getData();

        // ----------------- Get Profile -----------------
        assertNotNull(user.getProfile());
        Response <Profile> responseGetProfile = syncano.getObject(Profile.class, user.getProfile().getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetProfile.getHttpResultCode());
        assertNotNull(responseGetProfile.getData());

        // ----------------- Get One -----------------
        Response <User> responseGetUser = syncano.getUser(user.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetUser.getHttpResultCode());
        assertNotNull(responseGetUser.getData());
        assertEquals(user.getUserName(), responseGetUser.getData().getUserName());

        // ----------------- Get List -----------------
        Response <List<User>> responseGetUsers = syncano.getUsers().send();

        assertNotNull(responseGetUsers.getData());
        assertTrue("List should contain at least one item.", responseGetUsers.getData().size() > 0);

        // ----------------- Update -----------------
        user.setPassword(NEW_PASSWORD);
        Response <User> responseUpdateUser = syncano.updateUser(user).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateUser.getHttpResultCode());
        assertNotNull(responseUpdateUser.getData());
        assertEquals(user.getUserName(), responseUpdateUser.getData().getUserName());

        // ----------------- User Auth -----------------
        Response<User> responseUserAuth = syncano.authUser(USER_NAME, NEW_PASSWORD).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUserAuth.getHttpResultCode());
        assertNotNull(responseUserAuth.getData());
        assertNotNull(responseUserAuth.getData().getUserKey());

        // This is how user key should be used.
        // All next requests will be used using apiKey and userKey.
        syncano.setUserKey(responseUserAuth.getData().getUserKey());

        // ----------------- Delete -----------------
        Response <User> responseDeleteUser = syncano.deleteUser(user.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteUser.getHttpResultCode());
    }

    public void testCustomUsers() throws InterruptedException {

        // ----------------- Get Profile Class -----------------
        Response <SyncanoClass> responseGetProfileClass = syncano.getSyncanoClass(Constants.USER_PROFILE_CLASS_NAME).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetProfileClass.getHttpResultCode());
        assertNotNull(responseGetProfileClass.getData());
        SyncanoClass profileClass = responseGetProfileClass.getData();

        // ----------------- Update Profile Class -----------------
        profileClass.setSchema(TestUserProfileClass.getSchema());
        Response <TestUserProfileClass> responseUpdateProfileClass = syncano.updateSyncanoClass(profileClass).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateProfileClass.getHttpResultCode());
        assertNotNull(responseUpdateProfileClass.getData());


        final TestUserClass newUser = new TestUserClass(USER_NAME, PASSWORD);
        TestUserClass user;
        TestUserProfileClass testUserProfile;

        // ----------------- Create -----------------
        Response <TestUserClass> responseCreateUser = syncano.createCustomUser(newUser).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateUser.getHttpResultCode());
        assertNotNull(responseCreateUser.getData());
        user = responseCreateUser.getData();
        testUserProfile = responseCreateUser.getData().getProfile();

        // ----------------- Get Profile -----------------
        assertNotNull(user.getProfile());
        Response <TestUserProfileClass> responseGetProfile = syncano.getObject(TestUserProfileClass.class, user.getProfile().getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetProfile.getHttpResultCode());
        assertNotNull(responseGetProfile.getData());

        // ----------------- Update Profile -----------------
        testUserProfile.valueOne = "one";
        testUserProfile.valueTwo = "two";
        Response <TestSyncanoClass> responseUpdateProfile = syncano.updateObject(testUserProfile).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateProfile.getHttpResultCode());
        assertNotNull(responseUpdateProfile.getData());

        // ----------------- Get One -----------------
        Response <TestUserClass> responseGetUser = syncano.getCustomUser(TestUserClass.class, user.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetUser.getHttpResultCode());
        assertNotNull(responseGetUser.getData());
        assertNotNull(responseGetUser.getData().getProfile());
        assertEquals(testUserProfile.valueOne, responseGetUser.getData().getProfile().valueOne);
        assertEquals(testUserProfile.valueTwo, responseGetUser.getData().getProfile().valueTwo);

        // ----------------- User Auth -----------------
        Response<TestUserClass> responseUserAuth = syncano.authCustomUser(TestUserClass.class, USER_NAME, PASSWORD).send();

        assertEquals(responseUserAuth.getHttpReasonPhrase(), Response.HTTP_CODE_SUCCESS, responseUserAuth.getHttpResultCode());
        assertNotNull(responseUserAuth.getData());
        assertNotNull(responseUserAuth.getData().getUserKey());

        // ----------------- Delete -----------------
        Response <TestUserClass> responseDeleteUser = syncano.deleteUser(TestUserClass.class, user.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteUser.getHttpResultCode());
    }
}