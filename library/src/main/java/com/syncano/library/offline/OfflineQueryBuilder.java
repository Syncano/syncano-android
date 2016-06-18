package com.syncano.library.offline;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OfflineQueryBuilder {
    private String selection;
    private String[] selArgs;
    private String orderBy;

    public OfflineQueryBuilder(Context ctx, Class<? extends SyncanoObject> type, Where where, String orderBy) {
        if (where == null) {
            return;
        }
        JsonObject map = where.getQueryMap();
        ArrayList<String> expressions = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : map.entrySet()) {
            String column = findColumnName(type, entry.getKey());
            for (Map.Entry<String, JsonElement> expr : entry.getValue().getAsJsonObject().entrySet()) {
                boolean addValue = true;
                switch (expr.getKey()) {
                    case Where.FILTER_IS:
                        /*
                         * Inner query. It is additional call to db, because different classes are in
                         * different database files.
                         */
                        Field f = SyncanoClassHelper.findField(type, entry.getKey());
                        Where w = new Where();
                        w.setQueryMap(expr.getValue().getAsJsonObject());
                        List<SyncanoObject> refs = OfflineHelper.readObjects(ctx, (Class<? extends SyncanoObject>) f.getType(), w, null);
                        JsonArray ids = new JsonArray();
                        for (SyncanoObject so : refs) {
                            ids.add(so.getId());
                        }
                        addInFilter(column, expressions, values, ids);
                        addValue = false;
                        break;
                    case Where.FILTER_GT:
                        expressions.add("(" + column + ">?)");
                        break;
                    case Where.FILTER_GTE:
                        expressions.add("(" + column + ">=?)");
                        break;
                    case Where.FILTER_LT:
                        expressions.add("(" + column + "<?)");
                        break;
                    case Where.FILTER_LTE:
                        expressions.add("(" + column + "<=?)");
                        break;
                    case Where.FILTER_EQ:
                        expressions.add("(" + column + "==?)");
                        break;
                    case Where.FILTER_INS_EQ:
                        expressions.add("(" + column + "==? COLLATE NOCASE)");
                        break;
                    case Where.FILTER_NEQ:
                        expressions.add("(" + column + "!=?)");
                        break;
                    case Where.FILTER_EXISTS:
                        boolean exists = expr.getValue().getAsBoolean();
                        if (exists) {
                            expressions.add("(" + column + " IS NOT NULL)");
                        } else {
                            expressions.add("(" + column + " IS NULL)");
                        }
                        addValue = false;
                        break;
                    case Where.FILTER_IN:
                        addInFilter(column, expressions, values, expr.getValue().getAsJsonArray());
                        addValue = false;
                        break;
                    case Where.FILTER_START_WITH:
                        expressions.add("(" + column + " GLOB ?)");
                        values.add(expr.getValue().getAsString() + "*");
                        addValue = false;
                        break;
                    case Where.FILTER_INS_START_WITH:
                        expressions.add("(" + column + " LIKE ?)");
                        values.add(expr.getValue().getAsString() + "%");
                        addValue = false;
                        break;
                    case Where.FILTER_ENDS_WITH:
                        expressions.add("(" + column + " GLOB ?)");
                        values.add("*" + expr.getValue().getAsString());
                        addValue = false;
                        break;
                    case Where.FILTER_INS_ENDS_WITH:
                        expressions.add("(" + column + " LIKE ?)");
                        values.add("%" + expr.getValue().getAsString());
                        addValue = false;
                        break;
                    case Where.FILTER_CONTAINS:
                        expressions.add("(" + column + " GLOB ?)");
                        values.add("*" + expr.getValue().getAsString() + "*");
                        addValue = false;
                        break;
                    case Where.FILTER_INS_CONTAINS:
                        expressions.add("(" + column + " LIKE ?)");
                        values.add("%" + expr.getValue().getAsString() + "%");
                        addValue = false;
                        break;
                }
                if (addValue) values.add(expr.getValue().getAsString());
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expressions.size(); i++) {
            sb.append(expressions.get(i));
            if (i < expressions.size() - 1) {
                sb.append(" AND ");
            }
        }
        selection = sb.toString();
        selArgs = values.toArray(new String[values.size()]);
        if (orderBy != null) {
            if (orderBy.startsWith("-")) {
                this.orderBy = findColumnName(type, orderBy.substring(1)) + " DESC";
            } else {
                this.orderBy = findColumnName(type, orderBy) + " ASC";
            }
        }
    }

    private void addInFilter(String column, ArrayList<String> expressions, ArrayList<String> values, JsonArray arr) {
        StringBuilder sb = new StringBuilder("(");
        sb.append(column);
        sb.append(" IN (");
        for (int i = 0; i < arr.size(); i++) {
            values.add(arr.get(i).getAsString());
            sb.append('?');
            if (i < arr.size() - 1) sb.append(',');
        }
        sb.append("))");
        expressions.add(sb.toString());
    }

    private String findColumnName(Class<? extends SyncanoObject> type, String key) {
        return SyncanoClassHelper.getOfflineFieldName(SyncanoClassHelper.findField(type, key));
    }

    public String getSelection() {
        return selection;
    }

    public String[] getSelArgs() {
        return selArgs;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
