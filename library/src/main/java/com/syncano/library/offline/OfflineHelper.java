package com.syncano.library.offline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.parser.SyncanoObjectDeserializer;
import com.syncano.library.utils.NanosDate;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
            c.moveToNext();
        }
        return list;
    }

    public static <T extends SyncanoObject> void writeObjects(Context ctx, List<T> objects, Class<T> type) {
        SQLiteOpenHelper sqlHelper = getSQLiteOpenHelper(ctx, type);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        GsonParser.GsonParseConfig config = new GsonParser.GsonParseConfig();
        config.serializeReadOnlyFields = true;
        Gson gson = GsonParser.createGson(type, config);
        String tableName = getTableName(type);
        for (T object : objects) {
            JsonObject jsonized = gson.toJsonTree(object).getAsJsonObject();
            ContentValues values = new ContentValues();
            for (Map.Entry<String, JsonElement> entry : jsonized.entrySet()) {
                values.put(entry.getKey(), GsonParser.getJsonElementAsString(entry.getValue()));
            }
            // insert requires one column that is nullable, weird but has to live with it
            db.insert(tableName, SyncanoObject.FIELD_CHANNEL, values);
        }
    }

    private static <T extends SyncanoObject> void readField(T o, Cursor c, Field f) {
        String name = SyncanoClassHelper.getFieldName(f);
        int index = c.getColumnIndex(name);
        if (c.isNull(index)) {
            return;
        }
        f.setAccessible(true);
        try {
            Class<?> clazz = f.getType();
            if (clazz.equals(int.class) || clazz.equals(Integer.class)) {
                f.set(o, c.getInt(index));
            } else if (clazz.equals(byte.class) || clazz.equals(Byte.class)) {
                f.set(o, (byte) c.getInt(index));
            } else if (clazz.equals(short.class) || clazz.equals(Short.class)) {
                f.set(o, c.getShort(index));
            } else if (clazz.equals(String.class)) {
                f.set(o, c.getString(index));
            } else if (clazz.equals(Date.class) || clazz.equals(NanosDate.class)) {
                NanosDate nd = new NanosDate();
                nd.setFullNanos(c.getLong(index));
                f.set(o, nd);
            } else if (clazz.equals(boolean.class) || clazz.equals(Boolean.class)) {
                f.set(o, c.getInt(index) == 1);
            } else if (SyncanoObject.class.isAssignableFrom(clazz)) {
                //TODO
            } else if (clazz.equals(float.class) || clazz.equals(Float.class)) {
                f.set(o, c.getFloat(index));
            } else if (clazz.equals(double.class) || clazz.equals(Double.class)) {
                f.set(o, c.getDouble(index));
            } else if (clazz.equals(SyncanoFile.class)) {
                f.set(o, new SyncanoFile(c.getString(index)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            SyncanoLog.e(OfflineHelper.class.getSimpleName(), "Error getting data from db");
        }
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
            String fieldName = SyncanoClassHelper.getOfflineFieldName(f);
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

    public static <T extends SyncanoObject> void clearTable(Context ctx, Class<T> type) {
        SQLiteOpenHelper sqlHelper = getSQLiteOpenHelper(ctx, type);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.delete(getTableName(type), null, null);
    }
}
