package com.syncano.library.Model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

import java.util.Random;

@SyncanoClass(name = "something")
public class SomeObjectVersion1 extends SyncanoObject {
    public SomeObjectVersion1() {
    }

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

    public static SomeObjectVersion1 generateObject() {
        Random rnd = new Random();
        SomeObjectVersion1 o = new SomeObjectVersion1();
        o.someText = StringGenerator.generate(20);
        o.someInt = rnd.nextInt();
        o.someByte = (byte) rnd.nextInt();
        return o;
    }
}