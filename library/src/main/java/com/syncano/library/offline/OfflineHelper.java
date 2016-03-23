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
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class OfflineHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "syncano_storage";

    public static <T extends SyncanoObject> List<T> readObjects(Context ctx, final Class<T> type) {
        SQLiteOpenHelper sqlHelper = getSQLiteOpenHelper(ctx, type);
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        ArrayList<T> list = new ArrayList<>();
        Cursor c = db.query(getTableName(type), null, null, null, null, null, null);
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
