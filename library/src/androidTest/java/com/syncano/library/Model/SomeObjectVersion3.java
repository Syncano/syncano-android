package com.syncano.library.Model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "something", version = 3, previousVersion = SomeObjectVersion2.class)
public class SomeObjectVersion3 extends SyncanoObject {
    public SomeObjectVersion3(String text, int number, boolean bool) {
        someText = text;
        someInt = number;
        someBoolean = bool;
    }

    @SyncanoField(name = "some_text")
    public String someText;
    @SyncanoField(name = "some_int")
    public int someInt;
    @SyncanoField(name = "some_boolean")
    public boolean someBoolean;
}