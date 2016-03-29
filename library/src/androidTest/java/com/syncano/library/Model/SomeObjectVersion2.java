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

@SyncanoClass(name = "something", version = 2, previousVersion = SomeObjectVersion1.class)
public class SomeObjectVersion2 extends SyncanoObject {
    public SomeObjectVersion2() {
    }

    public SomeObjectVersion2(String text, int number, Date date) {
        someText = text;
        someInt = number;
        someDate = date;
    }

    @SyncanoField(name = "some_text")
    public String someText;
    @SyncanoField(name = "some_int")
    public int someInt;
    @SyncanoField(name = "some_date")
    public Date someDate;

    public static SomeObjectVersion2 generateObject() {
        Random rnd = new Random();
        SomeObjectVersion2 o = new SomeObjectVersion2();
        o.someText = StringGenerator.generate(20);
        o.someInt = rnd.nextInt();
        o.someDate = new Date();
        return o;
    }

    public static void migrate(int version) {
        if (version == 1) {
            Context ctx = Syncano.getInstance().getAndroidContext();
            List<SomeObjectVersion1> v1List = OfflineHelper.readObjects(ctx, SomeObjectVersion1.class);
            ArrayList<SomeObjectVersion2> v2List = new ArrayList<>();
            for (SomeObjectVersion1 v1 : v1List) {
                SomeObjectVersion2 v2 = new SomeObjectVersion2(v1.someText, v1.someInt, null);
                v2.setId(v1.getId());
                v2List.add(v2);
            }
            OfflineHelper.writeObjects(ctx, v2List, SomeObjectVersion2.class);
        }
    }
}