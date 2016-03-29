package com.syncano.library.Model;

import android.content.Context;

import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SyncanoClass(name = "something", version = 3, previousVersion = SomeObjectVersion2.class)
public class SomeObjectVersion3 extends SyncanoObject {
    public SomeObjectVersion3() {
    }

    public SomeObjectVersion3(String text, int number, Boolean bool) {
        someText = text;
        someInt = number;
        someBoolean = bool;
    }

    @SyncanoField(name = "some_text")
    public String someText;
    @SyncanoField(name = "some_int")
    public int someInt;
    @SyncanoField(name = "some_boolean")
    public Boolean someBoolean;


    public static SomeObjectVersion3 generateObject() {
        Random rnd = new Random();
        SomeObjectVersion3 o = new SomeObjectVersion3();
        o.someText = StringGenerator.generate(20);
        o.someInt = rnd.nextInt();
        o.someBoolean = rnd.nextBoolean();
        return o;
    }

    public static void migrate(int version) {
        switch (version) {
            case 1:
                SomeObjectVersion2.migrate(1);
            case 2:
                Context ctx = Syncano.getInstance().getAndroidContext();
                List<SomeObjectVersion2> v2List = OfflineHelper.readObjects(ctx, SomeObjectVersion2.class);
                ArrayList<SomeObjectVersion3> v3List = new ArrayList<>();
                for (SomeObjectVersion2 v2 : v2List) {
                    SomeObjectVersion3 v3 = new SomeObjectVersion3(v2.someText, v2.someInt, null);
                    v3.setId(v2.getId());
                    v3List.add(v3);
                }
                OfflineHelper.writeObjects(ctx, v3List, SomeObjectVersion3.class);
        }
    }
}