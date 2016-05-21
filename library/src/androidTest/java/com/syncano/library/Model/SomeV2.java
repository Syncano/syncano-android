package com.syncano.library.Model;

import android.content.Context;

import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SyncanoClass(name = "something", version = 2, previousVersion = SomeV1.class)
public class SomeV2 extends SyncanoObject {
    public final static String FIELD_TEXT = "some_text";
    public final static String FIELD_INT = "some_int";
    public final static String FIELD_DATE = "some_date";

    public SomeV2() {
    }

    public SomeV2(String text, int number, Date date) {
        someText = text;
        someInt = number;
        someDate = date;
    }

    @SyncanoField(name = FIELD_TEXT)
    public String someText;
    @SyncanoField(name = FIELD_INT)
    public int someInt;
    @SyncanoField(name = FIELD_DATE)
    public Date someDate;

    public static SomeV2 generateObject() {
        Random rnd = new Random();
        SomeV2 o = new SomeV2();
        o.someText = StringGenerator.generate(20);
        o.someInt = rnd.nextInt();
        o.someDate = new Date();
        return o;
    }

    public static void migrate(int version) {
        if (version == 1) {
            Context ctx = Syncano.getInstance().getAndroidContext();
            List<SomeV1> v1List = OfflineHelper.readObjects(ctx, SomeV1.class);
            ArrayList<SomeV2> v2List = new ArrayList<>();
            for (SomeV1 v1 : v1List) {
                SomeV2 v2 = new SomeV2(v1.someText, v1.someInt, null);
                v2.setId(v1.getId());
                v2List.add(v2);
            }
            OfflineHelper.writeObjects(ctx, v2List, SomeV2.class);
        }
    }

    public boolean fieldsEqual(SomeV2 other) {
        if (someText != null && someText.equals(other.someText)
                && someInt == other.someInt && someDate != null && someDate.equals(other.someDate)) {
            return true;
        }
        return false;
    }

    public static SomeV2 makeCopy(SomeV2 other) {
        return new SomeV2(other.someText, other.someInt, other.someDate);
    }
}