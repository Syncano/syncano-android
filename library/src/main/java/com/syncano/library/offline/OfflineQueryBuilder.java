package com.syncano.library.offline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.api.Where;

import java.util.ArrayList;
import java.util.Map;

public class OfflineQueryBuilder {
    private String selection;
    private String[] selArgs;
    private String orderBy;

    public OfflineQueryBuilder(Where where, String orderBy) {
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
            for (Map.Entry<String, JsonElement> expr : entry.getValue().entrySet()) {
                boolean addValue = true;
                switch (expr.getKey()) {
                    case Where.FILTER_IS:
                        // TODO add inner queries support
                        throw new RuntimeException("Inner filters are not yet supported in offline feature");
                    case Where.FILTER_GT:
                        expressions.add("(" + entry.getKey() + ">?)");
                        break;
                    case Where.FILTER_GTE:
                        expressions.add("(" + entry.getKey() + ">=?)");
                        break;
                    case Where.FILTER_LT:
                        expressions.add("(" + entry.getKey() + "<?)");
                        break;
                    case Where.FILTER_LTE:
                        expressions.add("(" + entry.getKey() + "<=?)");
                        break;
                    case Where.FILTER_EQ:
                        expressions.add("(" + entry.getKey() + "==?)");
                        break;
                    case Where.FILTER_INS_EQ:
                        expressions.add("(" + entry.getKey() + "==? COLLATE NOCASE)");
                        break;
                    case Where.FILTER_NEQ:
                        expressions.add("(" + entry.getKey() + "!=?)");
                        break;
                    case Where.FILTER_EXISTS:
                        boolean exists = expr.getValue().getAsBoolean();
                        if (exists) {
                            expressions.add("(" + entry.getKey() + " IS NOT NULL)");
                        } else {
                            expressions.add("(" + entry.getKey() + " IS NULL)");
                        }
                        addValue = false;
                        break;
                    case Where.FILTER_IN:
                        StringBuilder sb = new StringBuilder("(");
                        sb.append(entry.getKey());
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
                        expressions.add("(" + entry.getKey() + " GLOB ?)");
                        values.add(expr.getValue().getAsString() + "*");
                        addValue = false;
                        break;
                    case Where.FILTER_INS_START_WITH:
                        expressions.add("(" + entry.getKey() + " LIKE ?)");
                        values.add(expr.getValue().getAsString() + "%");
                        addValue = false;
                        break;
                    case Where.FILTER_ENDS_WITH:
                        expressions.add("(" + entry.getKey() + " GLOB ?)");
                        values.add("*" + expr.getValue().getAsString());
                        addValue = false;
                        break;
                    case Where.FILTER_INS_ENDS_WITH:
                        expressions.add("(" + entry.getKey() + " LIKE ?)");
                        values.add("%" + expr.getValue().getAsString());
                        addValue = false;
                        break;
                    case Where.FILTER_CONTAINS:
                        expressions.add("(" + entry.getKey() + " GLOB ?)");
                        values.add("*" + expr.getValue().getAsString() + "*");
                        addValue = false;
                        break;
                    case Where.FILTER_INS_CONTAINS:
                        expressions.add("(" + entry.getKey() + " LIKE ?)");
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
                this.orderBy = orderBy.substring(1) + " DESC";
            } else {
                this.orderBy = orderBy + " ASC";
            }
        }
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
