package com.syncano.library;

import com.syncano.library.Model.AllTypesObject;
import com.syncano.library.Model.SomeObjectVersion1;
import com.syncano.library.Model.SomeObjectVersion2;
import com.syncano.library.Model.SomeObjectVersion3;
import com.syncano.library.Model.SomeObjectVersion4;
import com.syncano.library.api.Response;
import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.List;

public class OfflineTest extends SyncanoApplicationTestCase {
    public void setUp() throws Exception {
        super.setUp();
        OfflineHelper.reinitHelper();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReadWrite() throws InterruptedException {
        createClass(SomeObjectVersion1.class);
        createClass(AllTypesObject.class);

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

        removeClass(AllTypesObject.class);
        removeClass(SomeObjectVersion1.class);
    }

    public void testMigration1To3() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion1.class);
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion3.class);

        ArrayList<SomeObjectVersion1> v1List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeObjectVersion1 o = SomeObjectVersion1.generateObject();
            o.setId(i);
            v1List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v1List, SomeObjectVersion1.class);
        List<SomeObjectVersion3> v3List = OfflineHelper.readObjects(getContext(), SomeObjectVersion3.class);

        assertEquals(v1List.size(), v3List.size());
        for (SomeObjectVersion1 v1 : v1List) {
            for (SomeObjectVersion3 v3 : v3List) {
                if (v1.getId().equals(v3.getId())) {
                    assertTrue(v1.someText.equals(v3.someText));
                    assertTrue(v1.someInt == v3.someInt);
                    assertNull(v3.someBoolean);
                }
            }
        }
    }

    public void testMigration2To3() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion1.class);
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion3.class);

        ArrayList<SomeObjectVersion2> v2List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeObjectVersion2 o = SomeObjectVersion2.generateObject();
            o.setId(i);
            v2List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v2List, SomeObjectVersion2.class);
        List<SomeObjectVersion3> v3List = OfflineHelper.readObjects(getContext(), SomeObjectVersion3.class);

        assertEquals(v2List.size(), v3List.size());
        for (SomeObjectVersion2 v2 : v2List) {
            for (SomeObjectVersion3 v3 : v3List) {
                if (v2.getId().equals(v3.getId())) {
                    assertTrue(v2.someText.equals(v3.someText));
                    assertTrue(v2.someInt == v3.someInt);
                    assertNull(v3.someBoolean);
                }
            }
        }
    }

    public void testMigration3To4() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion3.class);
        OfflineHelper.deleteDatabase(getContext(), SomeObjectVersion4.class);

        ArrayList<SomeObjectVersion3> v3List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeObjectVersion3 o = SomeObjectVersion3.generateObject();
            o.setId(i);
            v3List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v3List, SomeObjectVersion3.class);
        List<SomeObjectVersion4> v4List = OfflineHelper.readObjects(getContext(), SomeObjectVersion4.class);

        // v4 doesn't have migrate() method so the data will just be deleted
        assertTrue(v4List.size() == 0);
    }
}
