package com.syncano.android.lib.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.syncano.android.lib.utils.DateTool;

import java.util.Date;
import java.util.HashMap;
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
    }

    /**
     * Greater than.
     * Checking if value is greater than provided.
     * For string comparing length of the string.
     */
    public Where gt(String fieldName, Number value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than.
     * Checking if value is greater than provided.
     * For string comparing length of the string.
     */
    public Where gt(String fieldName, String value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than.
     * Checking if value is greater than provided.
     * For string comparing length of the string.
     */
    public Where gt(String fieldName, Date value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than or equal to.
     * Checking if value is greater than or equal to provided.
     * For string comparing length of the string.
     */
    public Where gte(String fieldName, Number value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Greater than or equal to.
     * Checking if value is greater than or equal to provided.
     * For string comparing length of the string.
     */
    public Where gte(String fieldName, String value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Greater than or equal to.
     * Checking if value is greater than or equal to provided.
     * For string comparing length of the string.
     */
    public Where gte(String fieldName, Date value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Less than.
     * Checking if value is lower than provided.
     * For string comparing length of the string.
     */
    public Where lt(String fieldName, Number value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than.
     * Checking if value is lower than provided.
     * For string comparing length of the string.
     */
    public Where lt(String fieldName, String value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than.
     * Checking if value is lower than provided.
     * For string comparing length of the string.
     */
    public Where lt(String fieldName, Date value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than or equal to.
     * Checking if value is lower or equal than provided.
     * For string comparing length of the string.
     */
    public Where lte(String fieldName, Number value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Less than or equal to.
     * Checking if value is lower or equal than provided.
     * For string comparing length of the string.
     */
    public Where lte(String fieldName, String value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Less than or equal to.
     * Checking if value is lower or equal than provided.
     * For string comparing length of the string.
     */
    public Where lte(String fieldName, Date value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where eq(String fieldName, Number value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where eq(String fieldName, Boolean value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where eq(String fieldName, String value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where eq(String fieldName, Date value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Not equal to.
     * Checking if value different than provided.
     * For string comparing length of the string.
     */
    public Where neq(String fieldName, Number value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Not equal to.
     * Checking if value different than provided.
     * For string comparing length of the string.
     */
    public Where neq(String fieldName, Boolean value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Not equal to.
     * Checking if value different than provided.
     * For string comparing length of the string.
     */
    public Where neq(String fieldName, String value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Not equal to.
     * Checking if value different than provided.
     * For string comparing length of the string.
     */
    public Where neq(String fieldName, Date value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Exist.
     * Checking if field is not empty.
     */
    public Where exists(String fieldName, Boolean exists) {
        addFilter(fieldName, FILTER_EXISTS, exists);
        return this;
    }

    /**
     * In a given list.
     * Checking if value of the field is on the provided list.
     * (list can contain up to 128 values)
     */
    public Where in(String fieldName, Number[] values) {
        JsonArray jsonArray = new JsonArray();

        for (Number element : values) {
            jsonArray.add(new JsonPrimitive(element));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

    /**
     * In a given list.
     * Checking if value of the field is on the provided list.
     * (list can contain up to 128 values)
     */
    public Where in(String fieldName, Boolean[] values) {
        JsonArray jsonArray = new JsonArray();

        for (Boolean element : values) {
            jsonArray.add(new JsonPrimitive(element));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

    /**
     * In a given list.
     * Checking if value of the field is on the provided list.
     * (list can contain up to 128 values)
     */
    public Where in(String fieldName, String[] values) {
        JsonArray jsonArray = new JsonArray();

        for (String element : values) {
            jsonArray.add(new JsonPrimitive(element));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

    /**
     * In a given list.
     * Checking if value of the field is on the provided list.
     * (list can contain up to 128 values)
     */
    public Where in(String fieldName, Date[] values) {
        JsonArray jsonArray = new JsonArray();

        for (Date element : values) {
            jsonArray.add(new JsonPrimitive(DateTool.parseDate(element)));
        }

        addFilter(fieldName, FILTER_IN, jsonArray);
        return this;
    }

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
