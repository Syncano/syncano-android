package com.syncano.library;

import com.syncano.library.Model.AllTypesObject;
import com.syncano.library.Model.SomeV1;
import com.syncano.library.Model.SomeV2;
import com.syncano.library.Model.SomeV3;
import com.syncano.library.Model.SomeV3StepMigration;
import com.syncano.library.Model.SomeV4;
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
        createClass(SomeV1.class);
        createClass(AllTypesObject.class);

        OfflineHelper.clearTable(getContext(), AllTypesObject.class);
        OfflineHelper.clearTable(getContext(), SomeV1.class);

        ArrayList<AllTypesObject> list = new ArrayList<>();
        ArrayList<SomeV1> someList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            AllTypesObject o = AllTypesObject.generateObject();
            Response<AllTypesObject> respRef = o.reference.save();
            assertTrue(respRef.isSuccess());
            list.add(respRef.getData());
            Response<SomeV1> respSomeRef = o.someReference.save();
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

        OfflineHelper.writeObjects(getContext(), someList, SomeV1.class);
        List<SomeV1> gotSome = OfflineHelper.readObjects(getContext(), SomeV1.class);
        assertEquals(someList.size(), gotSome.size());

        removeClass(AllTypesObject.class);
        removeClass(SomeV1.class);
    }

    public void testMigration1To3() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeV1.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV3.class);

        ArrayList<SomeV1> v1List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeV1 o = SomeV1.generateObject();
            o.setId(i);
            v1List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v1List, SomeV1.class);
        List<SomeV3> v3List = OfflineHelper.readObjects(getContext(), SomeV3.class);

        assertEquals(v1List.size(), v3List.size());
        for (SomeV1 v1 : v1List) {
            for (SomeV3 v3 : v3List) {
                if (v1.getId().equals(v3.getId())) {
                    assertTrue(v1.someText.equals(v3.someText));
                    assertTrue(v1.someInt == v3.someInt);
                    assertNull(v3.someBoolean);
                }
            }
        }
    }

    public void testStepMigration1To3() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeV1.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV3StepMigration.class);

        ArrayList<SomeV1> v1List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeV1 o = SomeV1.generateObject();
            o.setId(i);
            v1List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v1List, SomeV1.class);
        List<SomeV3StepMigration> v3List = OfflineHelper.readObjects(getContext(), SomeV3StepMigration.class);

        assertEquals(v1List.size(), v3List.size());
        for (SomeV1 v1 : v1List) {
            for (SomeV3StepMigration v3 : v3List) {
                if (v1.getId().equals(v3.getId())) {
                    assertTrue(v1.someText.equals(v3.someText));
                    assertTrue(v1.someInt == v3.someInt);
                    assertNull(v3.someBoolean);
                }
            }
        }
    }

    public void testMigration2To3() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeV1.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV3.class);

        ArrayList<SomeV2> v2List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeV2 o = SomeV2.generateObject();
            o.setId(i);
            v2List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v2List, SomeV2.class);
        List<SomeV3> v3List = OfflineHelper.readObjects(getContext(), SomeV3.class);

        assertEquals(v2List.size(), v3List.size());
        for (SomeV2 v2 : v2List) {
            for (SomeV3 v3 : v3List) {
                if (v2.getId().equals(v3.getId())) {
                    assertTrue(v2.someText.equals(v3.someText));
                    assertTrue(v2.someInt == v3.someInt);
                    assertNull(v3.someBoolean);
                }
            }
        }
    }

    public void testMigration3To4() throws InterruptedException {
        OfflineHelper.deleteDatabase(getContext(), SomeV2.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV3.class);
        OfflineHelper.deleteDatabase(getContext(), SomeV4.class);

        ArrayList<SomeV3> v3List = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SomeV3 o = SomeV3.generateObject();
            o.setId(i);
            v3List.add(o);
        }
        OfflineHelper.writeObjects(getContext(), v3List, SomeV3.class);
        List<SomeV4> v4List = OfflineHelper.readObjects(getContext(), SomeV4.class);

        // v4 doesn't have migrate() method so the data will just be deleted
        assertTrue(v4List.size() == 0);
    }
}
