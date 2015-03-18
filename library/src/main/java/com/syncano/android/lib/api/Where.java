package com.syncano.android.lib.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.syncano.android.lib.utils.DateTool;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Where {

    private static final String FILTER_GT = "_gt";
    private static final String FILTER_GTE = "_gte";
    private static final String FILTER_LT = "_lt";
    private static final String FILTER_LTE = "_lte";
    private static final String FILTER_EQ = "_eq";
    private static final String FILTER_NEQ = "_neq";
    private static final String FILTER_EXISTS = "_exists";
    private static final String FILTER_IN = "_in";

    private Map<String, JsonObject> query;

    public Where() {
        query = new HashMap<>();

        //eq("created_at", new Date(110000));
        //in("id", Arrays.asList(1, 2, 3, 4));

        //Log.d("WhereFilterJson", "json: query=" + buildQuery());
    }

    /**
     * Greater than.
     */
    public Where gt(String fieldName, Number value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    public Where gt(String fieldName, String value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    public Where gt(String fieldName, Date value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than or equal to.
     */
    public Where gte(String fieldName, Number value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    public Where gte(String fieldName, String value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    public Where gte(String fieldName, Date value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Less than.
     */
    public Where lt(String fieldName, Number value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    public Where lt(String fieldName, String value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    public Where lt(String fieldName, Date value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than or equal to.
     */
    public Where lte(String fieldName, Number value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    public Where lte(String fieldName, String value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    public Where lte(String fieldName, Date value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Equal to.
     */
    public Where eq(String fieldName, Number value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    public Where eq(String fieldName, Boolean value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    public Where eq(String fieldName, String value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    public Where eq(String fieldName, Date value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Not equal to.
     */
    public Where neq(String fieldName, Number value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    public Where neq(String fieldName, Boolean value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    public Where neq(String fieldName, String value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    public Where neq(String fieldName, Date value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Exist.
     */
    public Where exists(String fieldName, Boolean exists) {
        addFilter(fieldName, FILTER_EXISTS, exists);
        return this;
    }

    /**
     * In a given list.
     */
    /*public Where in(String fieldName, List<Integer> values) {
        JsonArray jsonArray = new JsonArray();

        for (Number element : values) {
            jsonArray.add(new JsonPrimitive(element));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

    public Where in(String fieldName, List<Boolean> values) {
        JsonArray jsonArray = new JsonArray();

        for (Boolean element : values) {
            jsonArray.add(new JsonPrimitive(element));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

    public Where in(String fieldName, List<String> values) {
        JsonArray jsonArray = new JsonArray();

        for (String element : values) {
            jsonArray.add(new JsonPrimitive(element));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

    public Where in(String fieldName, List<Date> values) {
        JsonArray jsonArray = new JsonArray();

        for (Date element : values) {
            jsonArray.add(new JsonPrimitive(DateTool.parseDate(element)));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }*/

    private void addFilter(String field, String filter, Number value) {
        addFilter(field, filter, new JsonPrimitive(value));
    }

    private void addFilter(String field, String filter, Boolean value) {
        addFilter(field, filter, new JsonPrimitive(value));
    }

    private void addFilter(String field, String filter, String value) {
        addFilter(field, filter, new JsonPrimitive(value));
    }

    private void addFilter(String field, String filter, Date date) {
        addFilter(field, filter, new JsonPrimitive(DateTool.parseDate(date)));
    }

    private void addFilter(String field, String filter, JsonElement value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(filter, value);
        query.put(field, jsonObject);
    }

    public String buildQuery() {
        return new Gson().toJson(query);
    }
}
