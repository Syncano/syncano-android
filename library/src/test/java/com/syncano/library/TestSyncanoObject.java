package com.syncano.library;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "testsyncanoclass")
public class TestSyncanoObject extends SyncanoObject {

    @SyncanoField(name = "value_one")
    public String valueOne;

    @SyncanoField(name = "value_two")
    public String valueTwo;

    public TestSyncanoObject() {
    }

    public TestSyncanoObject(String valueOne, String valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }
}
