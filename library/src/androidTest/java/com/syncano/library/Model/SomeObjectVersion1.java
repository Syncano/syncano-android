package com.syncano.library.Model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "something")
public class SomeObjectVersion1 extends SyncanoObject {
    public SomeObjectVersion1(String text, int number, byte byteNumber) {
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