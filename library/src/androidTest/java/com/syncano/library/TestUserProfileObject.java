package com.syncano.library;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = Constants.USER_PROFILE_CLASS_NAME)
public class TestUserProfileObject extends SyncanoObject {

    @SyncanoField(name = "value_one")
    public String valueOne;

    @SyncanoField(name = "value_two")
    public String valueTwo;

    public TestUserProfileObject() {
    }
}