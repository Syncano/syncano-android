package com.syncano.library;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.Profile;

public class TestUserProfileObject extends Profile {

    @SyncanoField(name = "value_one")
    public String valueOne;

    @SyncanoField(name = "value_two")
    public String valueTwo;

    public TestUserProfileObject() {
    }
}
