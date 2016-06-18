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

@SyncanoClass(name = "something", version = 3, previousVersion = SomeV2.class)
public class SomeV3 extends SyncanoObject {
    public SomeV3() {
    }

    public SomeV3(String text, int number, Boolean bool) {
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


    public static SomeV3 generateObject() {
        Random rnd = new Random();
        SomeV3 o = new SomeV3();
        o.someText = StringGenerator.generate(20);
        o.someInt = rnd.nextInt();
        o.someBoolean = rnd.nextBoolean();
        return o;
    }

    public static void migrate(int version) {
        switch (version) {
            case 1:
                SomeV2.migrate(1);
            case 2:
                Context ctx = Syncano.getInstance().getAndroidContext();
                List<SomeV2> v2List = OfflineHelper.readObjects(ctx, SomeV2.class);
                ArrayList<SomeV3> v3List = new ArrayList<>();
                for (SomeV2 v2 : v2List) {
                    SomeV3 v3 = new SomeV3(v2.someText, v2.someInt, null);
                    v3.setId(v2.getId());
                    v3List.add(v3);
                }
                OfflineHelper.writeObjects(ctx, v3List, SomeV3.class);
                OfflineHelper.deleteDatabase(ctx, SomeV2.class);
        }
    }
}