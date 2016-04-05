package com.syncano.library.Model;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;

@SyncanoClass(name = "something", version = 4, previousVersion = SomeV3.class)
public class SomeV4 extends SyncanoObject {
    public SomeV4() {
    }

    @SyncanoField(name = "some_text")
    public String someText;
}