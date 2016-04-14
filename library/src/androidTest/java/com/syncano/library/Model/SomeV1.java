package com.syncano.library.Model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

import java.util.Random;

@SyncanoClass(name = "something")
public class SomeV1 extends SyncanoObject {
    public SomeV1() {
    }

    public SomeV1(String text, int number, byte byteNumber) {
        someText = text;
        someInt = number;
        someByte = byteNumber;
    }

    @SyncanoField(name = "some_text")
    public String someText;
    @SyncanoField(name = "some_int", filterIndex = true)
    public int someInt;
    @SyncanoField(name = "some_byte")
    public byte someByte;

    public static SomeV1 generateObject() {
        Random rnd = new Random();
        SomeV1 o = new SomeV1();
        o.someText = StringGenerator.generate(20);
        o.someInt = rnd.nextInt();
        o.someByte = (byte) rnd.nextInt();
        return o;
    }
}