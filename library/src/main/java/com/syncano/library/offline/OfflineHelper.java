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
import com.syncano.library.api.Where;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OfflineHelper {
    private final static int VERSION = 1;
    private final static String TABLE_NAME = "syncano";
    private final static String MIGRATE_METHOD_NAME = "migrate";
    private static HashSet<String> checkedMigrations = new HashSet<>();

    public static <T extends SyncanoObject> List<T> readObjects(Context ctx, final Class<T> type) {
        return readObjects(ctx, type, null, null);
    }

    public static <T extends SyncanoObject> List<T> readObjects(Context ctx, Class<T> type, Where<T> where, String orderBy) {
        SQLiteOpenHelper sqlHelper = initDb(ctx, type);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        ArrayList<T> list = new ArrayList<>();
        OfflineQueryBuilder query = new OfflineQueryBuilder(where, orderBy);
        Cursor c = db.query(TABLE_NAME, null, query.getSelection(), query.getSelArgs(), null, null, query.getOrderBy());
        GsonParser.GsonParseConfig config = new GsonParser.GsonParseConfig();
        config.useOfflineFieldNames = true;
        Gson gson = GsonParser.createGson(type, config);
        String[] columns = c.getColumnNames();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            JsonObject json = new JsonObject();
            for (String column : columns) {
                json.addProperty(column, c.getString(c.getColumnIndex(column)));
            }
            list.add(gson.fromJson(json, type));
            c.moveToNext();
        }
        c.close();
        db.close();
        return list;
    }

    public static <T extends SyncanoObject> T readObject(Context ctx, Class<T> type, int id) {
        GsonParser.GsonParseConfig config = new GsonParser.GsonParseConfig();
        config.useOfflineFieldNames = true;
        Gson gson = GsonParser.createGson(type, config);
        return readObject(ctx, gson, type, id);
    }

    public static <T extends SyncanoObject> T readObject(Context ctx, T obj) {
        GsonParser.GsonParseConfig config = new GsonParser.GsonParseConfig();
        config.useOfflineFieldNames = true;
        Gson gson = GsonParser.createGson(obj, config);
        return (T) readObject(ctx, gson, obj.getClass(), obj.getId());
    }


    public static <T extends SyncanoObject> T readObject(Context ctx, Gson gson, Class<T> type, int id) {
        SQLiteOpenHelper sqlHelper = initDb(ctx, type);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        OfflineQueryBuilder query = new OfflineQueryBuilder(new Where<>().eq(Entity.FIELD_ID, id), null);
        Cursor c = db.query(TABLE_NAME, null, query.getSelection(), query.getSelArgs(), null, null, query.getOrderBy());

        String[] columns = c.getColumnNames();
        c.moveToFirst();
        T object = null;
        if (c.getCount() != 0) {
            JsonObject json = new JsonObject();
            for (String column : columns) {
                json.addProperty(column, c.getString(c.getColumnIndex(column)));
            }
            object = gson.fromJson(json, type);
        }
        c.close();
        db.close();
        return object;
    }

    public static void writeObjects(Context ctx, List<? extends SyncanoObject> objects, Class<? extends SyncanoObject> type) {
        SQLiteOpenHelper sqlHelper = initDb(ctx, type);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        GsonParser.GsonParseConfig config = new GsonParser.GsonParseConfig();
        config.serializeReadOnlyFields = true;
        config.serializeUrlFileFields = true;
        config.useOfflineFieldNames = true;
        Gson gson = GsonParser.createGson(type, config);
        for (SyncanoObject object : objects) {
            JsonObject jsonized = gson.toJsonTree(object).getAsJsonObject();
            ContentValues values = new ContentValues();
            for (Map.Entry<String, JsonElement> entry : jsonized.entrySet()) {
                values.put(entry.getKey(), GsonParser.getJsonElementAsString(entry.getValue()));
            }
            // insert requires one column that is nullable, weird but has to live with it
            db.insert(TABLE_NAME, SyncanoObject.FIELD_CHANNEL, values);
        }
        db.close();
    }

    private static SQLiteOpenHelper initDb(Context ctx, Class<? extends SyncanoObject> type) {
        String dbName = getDbName(type);
        if (type.getAnnotation(SyncanoClass.class).version() == 1 || checkedMigrations.contains(dbName)) {
            return getSQLiteOpenHelper(ctx, type);
        }

        LinkedList<Class<? extends SyncanoObject>> versions = new LinkedList<>();
        Class<? extends SyncanoObject> typeToCheck = type;
        do {
            versions.push(typeToCheck);
            typeToCheck = versions.peek().getAnnotation(SyncanoClass.class).previousVersion();
        } while (!migrate(ctx, versions, typeToCheck));

        return getSQLiteOpenHelper(ctx, type);
    }

    public static void reinitHelper() {
        checkedMigrations = new HashSet<>();
    }

    private static boolean migrate(Context ctx, LinkedList<Class<? extends SyncanoObject>> versions, Class<? extends SyncanoObject> oldTypeToCheck) {
        if (SyncanoClass.NOT_SET.class.equals(oldTypeToCheck)) {
            return true;
        }

        String oldDbName = getDbName(oldTypeToCheck);
        checkedMigrations.add(oldDbName);
        if (!dbExists(ctx, oldDbName)) {
            return false;
        }
        performMigration(ctx, versions, oldTypeToCheck);
        return true;
    }

    private static void performMigration(Context ctx, LinkedList<Class<? extends SyncanoObject>> versions, Class<? extends SyncanoObject> foundOldVersion) {
        Class<? extends SyncanoObject> type = versions.pollLast();
        checkedMigrations.add(getDbName(type));
        try {
            Method m = type.getMethod(MIGRATE_METHOD_NAME, int.class);
            m.invoke(null, foundOldVersion.getAnnotation(SyncanoClass.class).version());
        } catch (Exception e1) {
            // no migrate(int version) method so calling migrate()
            try {
                Method m = type.getMethod(MIGRATE_METHOD_NAME);
                if (!versions.isEmpty())
                    performMigration(ctx, versions, foundOldVersion);
                m.invoke(null);
            } catch (Exception e2) {
                // ignored, can be missing
            }
        }
        ctx.deleteDatabase(getDbName(foundOldVersion));
    }

    private static SQLiteOpenHelper getSQLiteOpenHelper(Context ctx, final Class<? extends SyncanoObject> type) {
        return new SQLiteOpenHelper(ctx, getDbName(type), null, VERSION) {
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

    private static boolean dbExists(Context ctx, String dbName) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(ctx.getDatabasePath(dbName).getPath(), null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            return true;
        } catch (Exception e) {
            if (checkDB != null) {
                checkDB.close();
            }
            // database doesn't exist yet
            return false;
        }
    }

    private static String generateCreateSql(Class<? extends SyncanoObject> type) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(TABLE_NAME);
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
            case DATETIME:
                return "TEXT";
            case INTEGER:
            case REFERENCE:
            case BOOLEAN:
                return "INTEGER";
            case FLOAT:
                return "REAL";
        }
        throw new RuntimeException("Can't get type of field " + SyncanoClassHelper.getFieldName(f));
    }

    private static String getDbName(Class<? extends SyncanoObject> type) {
        SyncanoClass syncanoClass = type.getAnnotation(SyncanoClass.class);
        return syncanoClass.name() + "_" + syncanoClass.version();
    }

    public static void clearTable(Context ctx, Class<? extends SyncanoObject> type) {
        SQLiteOpenHelper sqlHelper = initDb(ctx, type);
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }

    public static void deleteDatabase(Context ctx, Class<? extends SyncanoObject> type) {
        ctx.deleteDatabase(getDbName(type));
    }
}
