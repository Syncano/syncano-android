package com.syncano.library.offline;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.api.Where;

import java.util.ArrayList;
import java.util.Map;

public class OfflineQueryBuilder {
    String selection;
    String[] selArgs;
    String orderBy;

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
                // TODO add inner queries support
                if (!expr.getValue().isJsonPrimitive()) {
                    continue;
                }
                // TODO add the rest of possibilities
                switch (expr.getKey()) {
                    case Where.FILTER_EQ:
                        expressions.add("(" + entry.getKey() + "==?)");
                        values.add(expr.getValue().getAsString());
                        break;
                    case Where.FILTER_GT:
                        expressions.add("(" + entry.getKey() + ">?)");
                        values.add(expr.getValue().getAsString());
                        break;
                    case Where.FILTER_GTE:
                        expressions.add("(" + entry.getKey() + ">=?)");
                        values.add(expr.getValue().getAsString());
                        break;
                }
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
