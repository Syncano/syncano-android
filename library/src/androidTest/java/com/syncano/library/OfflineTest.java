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
        OfflineHelper.clearTable(getContext(), AllTypesObject.class);
        OfflineHelper.clearTable(getContext(), SomeObject.class);
    }

    public void tearDown() throws Exception {
        removeClass(AllTypesObject.class);
        removeClass(SomeObject.class);
        super.tearDown();
    }

    public void testOffline() throws InterruptedException {
        ArrayList<AllTypesObject> list = new ArrayList<>();
        ArrayList<SomeObject> someList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AllTypesObject o = AllTypesObject.generateObject();
            Response<AllTypesObject> respRef = o.reference.save();
            assertTrue(respRef.isSuccess());
            list.add(respRef.getData());
            Response<SomeObject> respSomeRef = o.someReference.save();
            assertTrue(respSomeRef.isSuccess());
            someList.add(respSomeRef.getData());
            Response<AllTypesObject> resp = o.save();
            assertTrue(resp.isSuccess());
            list.add(resp.getData());
        }

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

        OfflineHelper.writeObjects(getContext(), someList, SomeObject.class);
        List<SomeObject> gotSome = OfflineHelper.readObjects(getContext(), SomeObject.class);
        assertEquals(someList.size(), gotSome.size());
    }


}
