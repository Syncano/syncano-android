package com.syncano.library.offline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.api.Where;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class OfflineQueryBuilder {
    private String selection;
    private String[] selArgs;
    private String orderBy;

    public OfflineQueryBuilder(Class<? extends SyncanoObject> type, Where where, String orderBy) {
        if (where == null) {
            return;
        }
        Map<String, JsonObject> map = where.getQueryMap();
        if (map.isEmpty()) {
            return;
        }
        ArrayList<String> expressions = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();
        for (Map.Entry<String, JsonObject> entry : map.entrySet()) {
            String column = findColumnName(type, entry.getKey());
            for (Map.Entry<String, JsonElement> expr : entry.getValue().entrySet()) {
                boolean addValue = true;
                switch (expr.getKey()) {
                    case Where.FILTER_IS:
                        // TODO add inner queries support
                        throw new RuntimeException("Inner filters are not yet supported in offline feature");
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
                        StringBuilder sb = new StringBuilder("(");
                        sb.append(column);
                        sb.append(" IN (");
                        JsonArray arr = expr.getValue().getAsJsonArray();
                        for (int i = 0; i < arr.size(); i++) {
                            values.add(arr.get(i).getAsString());
                            sb.append('?');
                            if (i < arr.size() - 1) sb.append(',');
                        }
                        sb.append("))");
                        expressions.add(sb.toString());
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

    private String findColumnName(Class<? extends SyncanoObject> type, String key) {
        Collection<Field> fields = SyncanoClassHelper.findAllSyncanoFields(type);
        for (Field f : fields) {
            if (SyncanoClassHelper.getFieldName(f).equals(key)) {
                return SyncanoClassHelper.getOfflineFieldName(f);
            }
        }
        return null;
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
