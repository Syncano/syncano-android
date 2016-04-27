package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.data.AbstractUser;
import com.syncano.library.model.AllTypesProfile;
import com.syncano.library.model.Author;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ProfilesTest extends SyncanoApplicationTestCase {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testValuesinProfile() throws InterruptedException {
        createClass(Author.class);
        // update profile schema
        assertTrue(syncano.updateSyncanoClass(AllTypesProfile.class).send().isSuccess());

        // create user
        deleteTestUser(USERNAME);
        MyUser user = new MyUser(USERNAME, PASSWORD);
        assertTrue(user.register().isSuccess());

        // get profile id
        AllTypesProfile profile = user.getProfile();
        assertNotNull(profile);

        // generate values for profile
        AllTypesProfile.generateObject(profile);
        profile.reference = profile;

        // send new profile to syncano
        Response<AllTypesProfile> response = syncano.updateObject(profile, false).send();
        assertTrue(response.isSuccess());

        // check if returned the same values
        assertTrue(profile != response.getData());
        profile.checkEquals(response.getData(), false);
        assertNotNull(response.getData().file.getLink());
        assertNotNull(response.getData().otherFile.getLink());
    }

    public static class MyUser extends AbstractUser<AllTypesProfile> {
        public MyUser(String login, String pass) {
            super(login, pass);
        }

        public MyUser() {
        }
    }

}