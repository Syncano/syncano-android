package com.syncano.library;

import com.google.gson.annotations.SerializedName;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.NanosDate;

import java.util.Date;
import java.util.Random;

import static junit.framework.Assert.assertTrue;

@SyncanoClass(name = "all_types_object")
public class AllTypesObject extends SyncanoObject {
    @SyncanoField(name = "int")
    public int intVal;
    @SyncanoField(name = "intother")
    public int intOtherVal;
    @SyncanoField(name = "integerval")
    public Integer integerVal;
    @SyncanoField(name = "byte")
    public byte byteVal;
    @SyncanoField(name = "byteobj")
    public Byte byteObjVal;
    @SyncanoField(name = "short")
    public short shortVal;
    @SyncanoField(name = "shortobj")
    public Short shortObjVal;
    @SyncanoField(name = "enum")
    public SomeEnum someEnum;
    @SyncanoField(name = "enumgivenvalues")
    public EnumWithValues valuesEnum;
    @SyncanoField(name = "date")
    public Date date;
    @SyncanoField(name = "nanosdate")
    public NanosDate nanosDate;
    @SyncanoField(name = "string")
    public String stringVal;
    @SyncanoField(name = "text", type = FieldType.TEXT)
    public String text;
    @SyncanoField(name = "reference")
    public AllTypesObject reference;
    @SyncanoField(name = "other_reference")
    public SomeObject someReference;
    @SyncanoField(name = "yesorno")
    public boolean yesOrNo;
    @SyncanoField(name = "yesornobject")
    public Boolean yesOrNoObj;
    @SyncanoField(name = "float")
    public float someFloat;
    @SyncanoField(name = "floatobj")
    public Float someFloatObj;
    @SyncanoField(name = "double")
    public double someDouble;
    @SyncanoField(name = "doubleobj")
    public Double someDoubleObj;
    @SyncanoField(name = "file")
    public SyncanoFile file;
    @SyncanoField(name = "otherfile")
    public SyncanoFile otherFile;


    public enum SomeEnum {
        ONE_VALUE,
        OTHER_VALUE
    }

    public enum EnumWithValues {
        @SerializedName("one")ONE_VALUE,
        @SerializedName("other")OTHER_VALUE
    }

    public void checkEquals(Object other) {
        assertTrue(other != null);
        assertTrue(other instanceof AllTypesObject);
        AllTypesObject o = (AllTypesObject) other;
        assertTrue(same(intVal, o.intVal));
        assertTrue(same(intOtherVal, o.intOtherVal));
        assertTrue(same(integerVal, o.integerVal));
        assertTrue(same(byteVal, o.byteVal));
        assertTrue(same(byteObjVal, o.byteObjVal));
        assertTrue(same(shortVal, o.shortVal));
        assertTrue(same(shortObjVal, o.shortObjVal));
        assertTrue(same(someEnum, o.someEnum));
        assertTrue(same(valuesEnum, o.valuesEnum));
        assertTrue(same(date, o.date));
        assertTrue(same(nanosDate, o.nanosDate));
        assertTrue(same(stringVal, o.stringVal));
        assertTrue(same(text, o.text));
        assertTrue(same(yesOrNo, o.yesOrNo));
        assertTrue(same(yesOrNoObj, o.yesOrNoObj));
        assertTrue(sameFloat(someFloat, o.someFloat));
        assertTrue(sameFloat(someFloatObj, o.someFloatObj));
        assertTrue(sameDouble(someDouble, o.someDouble));
        assertTrue(sameDouble(someDoubleObj, o.someDoubleObj));
        assertTrue(sameReference(reference, o.reference));
        assertTrue(sameReference(someReference, o.someReference));
        assertTrue(sameFile(file, o.file));
        assertTrue(sameFile(otherFile, o.otherFile));
    }

    private boolean sameDouble(double a, double b) {
        double diff = a - b;
        return diff / a < 0.001d;
    }

    private boolean sameFloat(float a, float b) {
        float diff = a - b;
        return diff / a < 0.001f;
    }

    private boolean sameFile(SyncanoFile o1, SyncanoFile o2) {
        String link1 = null;
        if (o1 != null) {
            link1 = o1.getLink();
        }
        String link2 = null;
        if (o2 != null) {
            link2 = o2.getLink();
        }
        return same(link1, link2);
    }

    private boolean sameReference(SyncanoObject o1, SyncanoObject o2) {
        Integer id1 = null;
        if (o1 != null) {
            id1 = o1.getId();
        }
        Integer id2 = null;
        if (o2 != null) {
            id2 = o2.getId();
        }
        return same(id1, id2);
    }

    private boolean same(Object o1, Object o2) {
        if (o1 == o2) return true;
        if (o1 == null) return false;
        return o1.equals(o2);
    }

    public static AllTypesObject generateObject(Integer id, boolean withReference) {
        Random rnd = new Random();
        AllTypesObject o = new AllTypesObject();
        o.setId(id);
        o.intVal = rnd.nextInt();
        o.intOtherVal = 0;
        o.integerVal = rnd.nextInt();
        o.byteVal = (byte) rnd.nextInt();
        o.byteObjVal = (byte) rnd.nextInt();
        o.shortVal = (short) rnd.nextInt();
        o.shortObjVal = (short) rnd.nextInt();
        o.someEnum = SomeEnum.ONE_VALUE;
        o.valuesEnum = EnumWithValues.OTHER_VALUE;
        o.date = new Date();
        o.nanosDate = new NanosDate(o.date.getTime(), rnd.nextInt(999));
        o.stringVal = generateString(rnd.nextInt(128));
        o.text = generateString(rnd.nextInt(32000));
        if (withReference) {
            o.reference = generateObject(null, false);
        } else {
            o.reference = null;
        }
        o.someReference = new SomeObject(generateString(20), rnd.nextInt(), (byte) rnd.nextInt());
        o.yesOrNo = rnd.nextBoolean();
        o.yesOrNoObj = rnd.nextBoolean();
        o.someFloat = rnd.nextFloat();
        o.someFloatObj = rnd.nextFloat();
        o.someDouble = rnd.nextDouble();
        o.someDoubleObj = rnd.nextDouble();
        o.file = new SyncanoFile(generateString(50000).getBytes());
        o.otherFile = new SyncanoFile(generateString(50000).getBytes());
        return o;
    }

    public static AllTypesObject generateObject(Integer id) {
        return generateObject(id, true);
    }

    public static AllTypesObject generateObject() {
        return generateObject(null);
    }

    public static String generateString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz       ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}

