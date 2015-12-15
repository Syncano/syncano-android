package com.syncano.library.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.syncano.library.choice.Case;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.simple.ObjectPlease;
import com.syncano.library.utils.DateTool;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Where<T extends SyncanoObject> {

    private static final String FILTER_GT = "_gt";
    private static final String FILTER_GTE = "_gte";
    private static final String FILTER_LT = "_lt";
    private static final String FILTER_LTE = "_lte";
    private static final String FILTER_EQ = "_eq";
    private static final String FILTER_INS_EQ = "_ieq";
    private static final String FILTER_NEQ = "_neq";
    private static final String FILTER_EXISTS = "_exists";
    private static final String FILTER_IN = "_in";
    private static final String FILTER_START_WITH = "_startswith";
    private static final String FILTER_INS_START_WITH = "_istartswith";
    private static final String FILTER_ENDS_WITH = "_endswith";
    private static final String FILTER_INS_ENDS_WITH = "_iendswith";
    private static final String FILTER_CONTAINS = "_contains";
    private static final String FILTER_INS_CONTAINS = "_icontains";

    private Map<String, JsonObject> query;
    private ObjectPlease<T> please;

    public Where() {
        query = new HashMap<>();
    }

    public Where(ObjectPlease<T> please) {
        this();
        this.please = please;
    }

    public Response<List<T>> get() {
        if (please == null) {
            throw new RuntimeException("Can be called only from SyncanoObject.please(class).where().get()");
        }
        return please.get();
    }

    /**
     * Greater than.
     * Checking if value is greater than provided.
     * For string comparing length of the string.
     */
    public Where<T> gt(String fieldName, Number value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than.
     * Checking if value is greater than provided.
     * For string comparing length of the string.
     */
    public Where<T> gt(String fieldName, String value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than.
     * Checking if value is greater than provided.
     * For string comparing length of the string.
     */
    public Where<T> gt(String fieldName, Date value) {
        addFilter(fieldName, FILTER_GT, value);
        return this;
    }

    /**
     * Greater than or equal to.
     * Checking if value is greater than or equal to provided.
     * For string comparing length of the string.
     */
    public Where<T> gte(String fieldName, Number value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Greater than or equal to.
     * Checking if value is greater than or equal to provided.
     * For string comparing length of the string.
     */
    public Where<T> gte(String fieldName, String value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Greater than or equal to.
     * Checking if value is greater than or equal to provided.
     * For string comparing length of the string.
     */
    public Where<T> gte(String fieldName, Date value) {
        addFilter(fieldName, FILTER_GTE, value);
        return this;
    }

    /**
     * Less than.
     * Checking if value is lower than provided.
     * For string comparing length of the string.
     */
    public Where<T> lt(String fieldName, Number value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than.
     * Checking if value is lower than provided.
     * For string comparing length of the string.
     */
    public Where<T> lt(String fieldName, String value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than.
     * Checking if value is lower than provided.
     * For string comparing length of the string.
     */
    public Where<T> lt(String fieldName, Date value) {
        addFilter(fieldName, FILTER_LT, value);
        return this;
    }

    /**
     * Less than or equal to.
     * Checking if value is lower or equal than provided.
     * For string comparing length of the string.
     */
    public Where<T> lte(String fieldName, Number value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Less than or equal to.
     * Checking if value is lower or equal than provided.
     * For string comparing length of the string.
     */
    public Where<T> lte(String fieldName, String value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Less than or equal to.
     * Checking if value is lower or equal than provided.
     * For string comparing length of the string.
     */
    public Where<T> lte(String fieldName, Date value) {
        addFilter(fieldName, FILTER_LTE, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where<T> eq(String fieldName, Number value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where<T> eq(String fieldName, Boolean value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where<T> eq(String fieldName, String value) {
        return eq(fieldName, value, Case.SENSITIVE);
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where<T> eq(String fieldName, String value, Case caseSens) {
        String filterName = caseSens.getValue() ? FILTER_EQ : FILTER_INS_EQ;
        addFilter(fieldName, filterName, value);
        return this;
    }

    /**
     * Equal to.
     * Checking if value is equal than provided.
     */
    public Where<T> eq(String fieldName, Date value) {
        addFilter(fieldName, FILTER_EQ, value);
        return this;
    }

    /**
     * Not equal to.
     * Checking if value different than provided.
     * For string comparing length of the string.
     */
    public Where<T> neq(String fieldName, Number value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Not equal to.
     * Checking if value different than provided.
     * For string comparing length of the string.
     */
    public Where<T> neq(String fieldName, Boolean value) {
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
    public Where<T> neq(String fieldName, Date value) {
        addFilter(fieldName, FILTER_NEQ, value);
        return this;
    }

    /**
     * Starts with.
     * Checks if a string starts with the given query.
     */
    public Where<T> startsWith(String fieldName, String value) {
        return startsWith(fieldName, value, Case.SENSITIVE);
    }

    /**
     * Starts with.
     * Checks if a string starts with the given query.
     */
    public Where<T> startsWith(String fieldName, String value, Case caseSens) {
        String filterName = caseSens.getValue() ? FILTER_START_WITH : FILTER_INS_START_WITH;
        addFilter(fieldName, filterName, value);
        return this;
    }

    /**
     * Ends with.
     * Checks if a string ends with the given query.
     */
    public Where<T> endsWith(String fieldName, String value) {
        return endsWith(fieldName, value, Case.SENSITIVE);
    }

    /**
     * Ends with.
     * Checks if a string ends with the given query.
     */
    public Where<T> endsWith(String fieldName, String value, Case caseSens) {
        String filterName = caseSens.getValue() ? FILTER_ENDS_WITH : FILTER_INS_ENDS_WITH;
        addFilter(fieldName, filterName, value);
        return this;
    }

    /**
     * {@link #contains(String, String, Case)}
     */
    public Where<T> contains(String fieldName, String value) {
        return contains(fieldName, value, Case.SENSITIVE);
    }

    /**
     * Contains.
     * Checks if a string contains the given query.
     */
    public Where<T> contains(String fieldName, String value, Case caseSens) {
        String filterName = caseSens.getValue() ? FILTER_CONTAINS : FILTER_INS_CONTAINS;
        addFilter(fieldName, filterName, value);
        return this;
    }

    /**
     * Exist.
     * Checking if field is not empty.
     */
    public Where<T> exists(String fieldName, Boolean exists) {
        addFilter(fieldName, FILTER_EXISTS, exists);
        return this;
    }

    /**
     * In a given list.
     * Checking if value of the field is on the provided list.
     * (list can contain up to 128 values)
     */
    public Where<T> in(String fieldName, Number[] values) {
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
    public Where<T> in(String fieldName, Boolean[] values) {
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
    public Where<T> in(String fieldName, String[] values) {
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
    public Where<T> in(String fieldName, Date[] values) {
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

        JsonObject jsonObject = query.get(field);

        if (jsonObject == null) {
            jsonObject = new JsonObject();
            query.put(field, jsonObject);
        }

        jsonObject.add(filter, value);
    }

    public String buildQuery() {
        return new Gson().toJson(query);
    }

}
