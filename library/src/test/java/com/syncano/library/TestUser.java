package com.syncano.library;

import com.syncano.library.data.AbstractUser;

public class TestUser extends AbstractUser<TestUserProfileObject> {

    public TestUser() {
    }

    public TestUser(String userName, String password) {
        super(userName, password);
    }
}
