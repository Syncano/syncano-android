package com.syncano.library;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineTest extends SyncanoApplicationTestCase {
    public void setUp() throws Exception {
        super.setUp();
        createClass(Something.class);
    }

    public void tearDown() throws Exception {
        removeClass(Something.class);
        super.tearDown();
    }

    public void testOffline() throws InterruptedException {
        OfflineHelper.clearTable(getContext(), Something.class);

        Something s1 = new Something();
        s1.setId(10);
        s1.someByte = 5;
        s1.someText = "aa";
        s1.someInt = 12;
        Something s2 = new Something();
        s2.setId(11);
        s2.someByte = 2;
        s2.someText = "oo";
        s2.someInt = 112;
        ArrayList<Something> soms = new ArrayList<>();
        soms.add(s1);
        soms.add(s2);
        OfflineHelper.writeObjects(getContext(), soms, Something.class);

        List<Something> got = OfflineHelper.readObjects(getContext(), Something.class);
        got.size();
    }

    @com.syncano.library.annotation.SyncanoClass(name = "just_something")
    private static class Something extends SyncanoObject {
        @SyncanoField(name = "some_text")
        public String someText;
        @SyncanoField(name = "some_int")
        public int someInt;
        @SyncanoField(name = "some_byte")
        public byte someByte;
    }
}
