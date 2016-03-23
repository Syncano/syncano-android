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

@SyncanoClass(name = "all_types_object")
public class AllTypesObject extends SyncanoObject {
    @SyncanoField(name = "int")
    public int intVal;
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

    public static AllTypesObject generateObject(boolean withReference) {
        Random rnd = new Random();
        AllTypesObject o = new AllTypesObject();
        o.intVal = rnd.nextInt();
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
            o.reference = generateObject(false);
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

    public static AllTypesObject generateObject() {
        return generateObject(true);
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

