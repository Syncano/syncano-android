package com.syncano.library;

import com.syncano.library.data.AbstractUser;

public class TestUserClass extends AbstractUser<TestUserProfileClass> {

    public TestUserClass() {
    }

    public TestUserClass(String userName, String password) {
        super(userName, password);
    }
}
