package com.syncano.library.offline;

import android.content.Context;

import com.syncano.library.PlatformType;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class LocalFieldsHelper {
    public static void fillWithLocalData(Context ctx, Collection<? extends SyncanoObject> objects) {
        if (!PlatformType.isAndroid()) {
            return;
        }
        for (SyncanoObject obj : objects) {
            fillWithLocalData(ctx, obj);
        }
    }

    public static void fillWithLocalData(Context ctx, SyncanoObject obj) {
        if (!PlatformType.isAndroid()) {
            return;
        }
        Collection<Field> localFields = findLocalFields(obj.getClass());
        if (localFields.size() == 0) {
            return;
        }
        SyncanoObject offlineObject = OfflineHelper.readObject(ctx, obj.getClass(), obj.getId());
        if (offlineObject == null) {
            return;
        }
        try {
            for (Field f : localFields) {
                f.setAccessible(true);
                f.set(obj, f.get(offlineObject));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static Collection<Field> findLocalFields(Class type) {
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(type);
        ArrayList<Field> locals = new ArrayList<>();
        for (Field f : fields) {
            SyncanoField fieldAnnotation = f.getAnnotation(SyncanoField.class);
            if (fieldAnnotation.onlyLocal()) {
                locals.add(f);
            }
        }
        return locals;
    }
}
