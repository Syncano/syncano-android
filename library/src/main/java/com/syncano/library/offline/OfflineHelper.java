package com.syncano.library.offline;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.parser.SyncanoObjectDeserializer;
import com.syncano.library.utils.NanosDate;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OfflineHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "syncano";

    public static <T extends SyncanoObject> List<T> readObjects(Context ctx, final Class<T> type) {
        SQLiteOpenHelper sqlHelper = getSQLiteOpenHelper(ctx, type);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(type);
        ArrayList<T> list = new ArrayList<>();
        Cursor c = db.query(getTableName(type), null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            T o = (T) SyncanoObjectDeserializer.createSyncanoObject(type);
            for (Field f : fields) {
                readField(o, c, f);
            }
            list.add(o);
        }
        return list;
    }

    private static <T extends SyncanoObject> void readField(T o, Cursor c, Field f) {
        FieldType type = SyncanoClassHelper.findType(f);
        if (type == null || type == FieldType.NOT_SET) {
            throw new RuntimeException("Can't get type of field " + SyncanoClassHelper.getFieldName(f));
        }
        f.setAccessible(true);
        String name = SyncanoClassHelper.getFieldName(f);
        int index = c.getColumnIndex(name);
        try {
            // TODO add all supported types, example: long, byte, short
            switch (type) {
                case STRING:
                case TEXT:
                    f.set(o, c.getString(index));
                    break;
                case FILE:
                    f.set(o, new SyncanoFile(c.getString(index)));
                    break;
                case INTEGER:
                    f.set(o, c.getInt(index));
                    break;
                case REFERENCE:
                    //TODO
                case BOOLEAN:
                    f.set(o, c.getInt(index) == 1);
                    break;
                case DATETIME:
                    NanosDate nd = new NanosDate();
                    nd.setFullNanos(c.getLong(index));
                    f.set(o, nd);
                    break;
                case FLOAT:
                    f.set(o, c.getFloat(index));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            SyncanoLog.e(OfflineHelper.class.getSimpleName(), "Error getting data from db");
        }
    }


    private static <T extends SyncanoObject> String[] getProjection(Class<T> type) {
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(type);
        ArrayList<String> projection = new ArrayList<>();
        for (Field f : fields) {
            projection.add(SyncanoClassHelper.getFieldName(f));
        }
        return projection.toArray(new String[projection.size()]);
    }

    private static <T extends SyncanoObject> SQLiteOpenHelper getSQLiteOpenHelper(Context ctx, final Class<T> type) {
        return new SQLiteOpenHelper(ctx, DB_NAME, null, VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                String createSql = generateCreateSql(type);
                db.execSQL(createSql);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
        };
    }

    private static <T extends SyncanoObject> String generateCreateSql(Class<T> type) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(getTableName(type));
        sb.append(" (");
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(type);
        for (Field f : fields) {
            String fieldName = SyncanoClassHelper.getFieldName(f);
            if (fieldName.equals(Entity.FIELD_ID)) continue;
            sb.append(fieldName);
            sb.append(' ');
            sb.append(getFieldSqlTypeName(f));
            sb.append(", ");
        }
        sb.append(Entity.FIELD_ID);
        sb.append(" INTEGER PRIMARY KEY )");
        return sb.toString();
    }

    private static String getFieldSqlTypeName(Field f) {
        FieldType type = SyncanoClassHelper.findType(f);
        if (type == null || type == FieldType.NOT_SET) {
            throw new RuntimeException("Can't get type of field " + SyncanoClassHelper.getFieldName(f));
        }
        switch (type) {
            case STRING:
            case TEXT:
            case FILE:
                return "TEXT";
            case INTEGER:
            case REFERENCE:
            case BOOLEAN:
            case DATETIME:
                return "INTEGER";
            case FLOAT:
                return "REAL";
        }
        throw new RuntimeException("Can't get type of field " + SyncanoClassHelper.getFieldName(f));
    }

    private static <T extends SyncanoObject> String getTableName(Class<T> type) {
        SyncanoClass syncanoClass = type.getAnnotation(SyncanoClass.class);
        return syncanoClass.name() + "_" + syncanoClass.version();
    }
}
