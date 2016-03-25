package com.syncano.library.Model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

import java.util.Date;

@SyncanoClass(name = "something", version = 2, previousVersion = SomeObjectVersion1.class)
public class SomeObjectVersion2 extends SyncanoObject {
    public SomeObjectVersion2(String text, int number, Date date) {
        someText = text;
        someInt = number;
        someDate = date;
    }

    @SyncanoField(name = "some_text")
    public String someText;
    @SyncanoField(name = "some_int")
    public int someInt;
    @SyncanoField(name = "some_date")
    public Date someDate;
}