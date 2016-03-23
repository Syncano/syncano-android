package com.syncano.library;

import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineTest extends SyncanoApplicationTestCase {
    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testOffline() throws InterruptedException {
        OfflineHelper.clearTable(getContext(), AllTypesObject.class);

        ArrayList<AllTypesObject> list = new ArrayList<>();
        list.add(AllTypesObject.generateObject());
        list.add(AllTypesObject.generateObject());
        OfflineHelper.writeObjects(getContext(), list, AllTypesObject.class);

        List<AllTypesObject> got = OfflineHelper.readObjects(getContext(), AllTypesObject.class);
        got.size();
    }


}
