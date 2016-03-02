package com.syncano.library;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "testsyncanoclass")
public class TestSyncanoObject extends SyncanoObject {

    public static final String FIELD_VAL_ONE = "value_one";
    public static final String FIELD_VAL_TWO = "value_two";

    @SyncanoField(name = FIELD_VAL_ONE, filterIndex = true)
    public String valueOne;

    @SyncanoField(name = FIELD_VAL_TWO)
    public String valueTwo;

    public TestSyncanoObject() {
    }

    public TestSyncanoObject(String valueOne, String valueTwo) {
        this.valueOne = valueOne;
        this.valueTwo = valueTwo;
    }
}
