package com.syncano.library;

import com.syncano.library.data.AbstractUser;
import com.syncano.library.data.Profile;

public class TestUserClass extends AbstractUser<TestUserProfileClass> {

    public TestUserClass() {
    }

    public TestUserClass(String userName, String password) {
        super(userName, password);
    }
}
