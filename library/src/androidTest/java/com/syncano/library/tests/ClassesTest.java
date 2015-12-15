package com.syncano.library.tests;

import com.syncano.library.Constants;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.NanosDate;
import com.syncano.library.utils.SyncanoClassHelper;

import java.util.Date;
import java.util.List;
import java.util.Random;

public class ClassesTest extends SyncanoApplicationTestCase {

    public void testClasses() throws InterruptedException {

        String className = SyncanoClassHelper.getSyncanoClassName(TestSyncanoObject.class);
        Response<SyncanoClass> respDelete = syncano.deleteSyncanoClass(className).send();
        assertTrue(respDelete.isSuccess());

        SyncanoClass syncanoClass = new SyncanoClass(TestSyncanoObject.class);
        syncanoClass.setDescription("First description");

        // ----------------- Create -----------------

        Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
        assertTrue(responseCreateClass.isSuccess());
        assertNotNull(responseCreateClass.getData());
        syncanoClass = responseCreateClass.getData();

        // ----------------- Get One -----------------
        Response<SyncanoClass> responseGetClass = syncano.getSyncanoClass(syncanoClass.getName()).send();
        assertTrue(responseGetClass.isSuccess());
        assertNotNull(responseGetClass.getData());
        assertEquals(className, responseGetClass.getData().getName());
        assertEquals(SyncanoClassHelper.getSyncanoClassSchema(TestSyncanoObject.class), responseGetClass.getData().getSchema());

        // ----------------- Update -----------------
        String description = "Second description";
        syncanoClass.setDescription(description);
        Response<SyncanoClass> responseUpdateClass = syncano.updateSyncanoClass(syncanoClass).send();
        assertTrue(responseUpdateClass.isSuccess());
        assertNotNull(responseUpdateClass.getData());
        assertEquals(responseUpdateClass.getData().getDescription(), description);


        // ----------------- Get List -----------------
        Response<List<SyncanoClass>> responseGetClasses = syncano.getSyncanoClasses().send();

        assertNotNull(responseGetClasses.getData());
        assertTrue("List should contain at least one item.", responseGetClasses.getData().size() > 0);

        // ----------------- Delete -----------------
        Response responseDeleteClass = syncano.deleteSyncanoClass(className).send();

        assertTrue(responseDeleteClass.isSuccess());
    }

    public void testDataTypes() {
        syncano.deleteSyncanoClass(MultiTypesObject.class).send();
        Response<SyncanoClass> respClass = syncano.createSyncanoClass(MultiTypesObject.class).send();
        assertTrue(respClass.isSuccess());

        MultiTypesObject obj1 = generateMultiTypesObject();
        Response<MultiTypesObject> resp1 = syncano.createObject(obj1).send();
        assertTrue(resp1.isSuccess());

        MultiTypesObject serverObj1 = resp1.getData();
        assertNotNull(serverObj1);

        assertEquals(obj1.intVal, serverObj1.intVal);
        assertEquals(obj1.byteVal, serverObj1.byteVal);
        assertEquals(obj1.shortVal, serverObj1.shortVal);
        assertEquals(obj1.date, serverObj1.date);
        assertEquals(obj1.nanosDate, serverObj1.nanosDate);
        assertEquals(obj1.stringVal.length(), serverObj1.stringVal.length());
        assertEquals(obj1.stringVal, serverObj1.stringVal);
        assertEquals(obj1.text.length(), serverObj1.text.length());
        assertEquals(obj1.text, serverObj1.text);
        assertEquals(obj1.reference, serverObj1.reference);
        assertEquals(obj1.yesOrNo, serverObj1.yesOrNo);
    }

    private MultiTypesObject generateMultiTypesObject() {
        Random rnd = new Random();
        MultiTypesObject o = new MultiTypesObject();
        o.intVal = rnd.nextInt();
        o.byteVal = (byte) rnd.nextInt();
        o.shortVal = (short) rnd.nextInt();
        o.date = new Date();
        o.nanosDate = new NanosDate(o.date.getTime(), rnd.nextInt(999));
        o.stringVal = generateString(rnd.nextInt(128));
        o.text = generateString(rnd.nextInt(32000));
        o.yesOrNo = rnd.nextBoolean();
        return o;
    }

    @com.syncano.library.annotation.SyncanoClass(name = "multi_typed_object")
    public static class MultiTypesObject extends SyncanoObject {
        @SyncanoField(name = "int")
        public int intVal;
        @SyncanoField(name = "byte")
        public byte byteVal;
        @SyncanoField(name = "short")
        public short shortVal;
        @SyncanoField(name = "date")
        public Date date;
        @SyncanoField(name = "nanosdate")
        public NanosDate nanosDate;
        @SyncanoField(name = "string")
        public String stringVal;
        @SyncanoField(name = "text", type = FieldType.TEXT)
        public String text;
        @SyncanoField(name = "reference", type = FieldType.REFERENCE, target = Constants.FIELD_TARGET_SELF)
        public Integer reference;
        @SyncanoField(name = "yesorno")
        public boolean yesOrNo;
    }
}