package com.syncano.library.model;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.Profile;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.NanosDate;

import java.util.Date;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class AllTypesProfile extends Profile {
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
    @SyncanoField(name = "shortprimitive")
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
    public AllTypesProfile reference;
    @SyncanoField(name = "other_reference")
    public Author someReference;
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

    public void checkEquals(Object other, boolean checkFiles) {
        assertTrue(other != null);
        assertTrue(other instanceof AllTypesProfile);
        AllTypesProfile o = (AllTypesProfile) other;
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
        if (checkFiles) {
            assertTrue(sameFile(file, o.file));
            assertTrue(sameFile(otherFile, o.otherFile));
        }
    }

    private boolean sameDouble(Double a, Double b) {
        if (a == b) return true;
        if (a.equals(b)) return true;
        double diff = a - b;
        return diff / a < 0.001d;
    }

    private boolean sameFloat(Float a, Float b) {
        if (a == b) return true;
        if (a.equals(b)) return true;
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
        // TODO When cleared string it is not null, but "". Maybe update it?
        if ((o1 instanceof String && o2 == null && ((String) o1).isEmpty())
                || (o2 instanceof String && o1 == null && ((String) o2).isEmpty())) {
            return true;
        }
        if (o1 == null) return false;
        return o1.equals(o2);
    }

    public static AllTypesProfile generateObject(AllTypesProfile o) {
        Random rnd = new Random();
        o.intVal = rnd.nextInt();
        o.intOtherVal = rnd.nextInt();
        o.integerVal = rnd.nextInt();
        o.byteVal = (byte) rnd.nextInt();
        o.byteObjVal = (byte) rnd.nextInt();
        o.shortVal = (short) rnd.nextInt();
        o.shortObjVal = (short) rnd.nextInt();
        o.someEnum = SomeEnum.ONE_VALUE;
        o.valuesEnum = EnumWithValues.OTHER_VALUE;
        o.date = new Date();
        o.nanosDate = new NanosDate(o.date.getTime(), rnd.nextInt(999));
        o.stringVal = StringGenerator.generate(rnd.nextInt(128));
        o.text = StringGenerator.generate(rnd.nextInt(32000));
        o.reference = null;
        o.someReference = new Author();
        o.someReference.birthDate = new Date();
        o.someReference.isMale = rnd.nextBoolean();
        o.someReference.name = StringGenerator.generate(15);
        o.yesOrNo = rnd.nextBoolean();
        o.yesOrNoObj = rnd.nextBoolean();
        o.someFloat = rnd.nextFloat();
        o.someFloatObj = rnd.nextFloat();
        o.someDouble = rnd.nextDouble();
        o.someDoubleObj = rnd.nextDouble();
        o.file = new SyncanoFile(StringGenerator.generate(50000).getBytes());
        o.otherFile = new SyncanoFile(StringGenerator.generate(50000).getBytes());
        return o;
    }

    public void clearAll() {
        clearField("int");
        clearField("intother");
        clearField("integerval");
        clearField("byte");
        clearField("byteobj");
        clearField("shortprimitive");
        clearField("shortobj");
        clearField("enum");
        clearField("enumgivenvalues");
        clearField("date");
        clearField("nanosdate");
        clearField("string");
        clearField("text");
        clearField("reference");
        clearField("other_reference");
        clearField("yesorno");
        clearField("yesornobject");
        clearField("float");
        clearField("floatobj");
        clearField("double");
        clearField("doubleobj");
        clearField("file");
        clearField("otherfile");
    }
}