package com.syncano.library;

import com.syncano.library.Model.AllTypesObject;
import com.syncano.library.Model.SomeObjectVersion1;
import com.syncano.library.api.Response;
import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineTest extends SyncanoApplicationTestCase {
    public void setUp() throws Exception {
        super.setUp();
        createClass(SomeObjectVersion1.class);
        createClass(AllTypesObject.class);
    }

    public void tearDown() throws Exception {
        removeClass(AllTypesObject.class);
        removeClass(SomeObjectVersion1.class);
        super.tearDown();
    }

    public void testReadWrite() throws InterruptedException {
        OfflineHelper.clearTable(getContext(), AllTypesObject.class);
        OfflineHelper.clearTable(getContext(), SomeObjectVersion1.class);

        ArrayList<AllTypesObject> list = new ArrayList<>();
        ArrayList<SomeObjectVersion1> someList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AllTypesObject o = AllTypesObject.generateObject();
            Response<AllTypesObject> respRef = o.reference.save();
            assertTrue(respRef.isSuccess());
            list.add(respRef.getData());
            Response<SomeObjectVersion1> respSomeRef = o.someReference.save();
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

        OfflineHelper.writeObjects(getContext(), someList, SomeObjectVersion1.class);
        List<SomeObjectVersion1> gotSome = OfflineHelper.readObjects(getContext(), SomeObjectVersion1.class);
        assertEquals(someList.size(), gotSome.size());
    }

    public void testMigration() {

    }
}
