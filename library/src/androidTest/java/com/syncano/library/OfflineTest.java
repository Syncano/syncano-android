package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineTest extends SyncanoApplicationTestCase {
    public void setUp() throws Exception {
        super.setUp();
        createClass(SomeObject.class);
        createClass(AllTypesObject.class);
    }

    public void tearDown() throws Exception {
        removeClass(AllTypesObject.class);
        removeClass(SomeObject.class);
        super.tearDown();
    }

    public void testOffline() throws InterruptedException {

        ArrayList<AllTypesObject> list = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Response<AllTypesObject> resp = AllTypesObject.generateObject(null).save();
            assertTrue(resp.isSuccess());
            list.add(resp.getData());
        }

        OfflineHelper.clearTable(getContext(), AllTypesObject.class);
        OfflineHelper.writeObjects(getContext(), list, AllTypesObject.class);

        List<AllTypesObject> got = OfflineHelper.readObjects(getContext(), AllTypesObject.class);
        assertEquals(list.size(), got.size());

        for (AllTypesObject o1 : list) {
            for (AllTypesObject o2 : got) {
                if (o1.getId().equals(o2.getId())) {
                    o1.checkEquals(o2);
                }
            }
        }
    }


}
