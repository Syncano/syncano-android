package com.syncano.library;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "just_something")
public class SomeObject extends SyncanoObject {
    public SomeObject(String text, int number, byte byteNumber) {
        someText = text;
        someInt = number;
        someByte = byteNumber;
    }

    @SyncanoField(name = "some_text")
    public String someText;
    @SyncanoField(name = "some_int")
    public int someInt;
    @SyncanoField(name = "some_byte")
    public byte someByte;
}