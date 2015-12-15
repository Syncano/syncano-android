package com.syncano.library.api;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public final class CounterBuilder {
    private final static String SYNCANO_INCREMENT_OR_DECREMENT = "_increment";
    private HashMap<String, Integer> changeCounterField;

    public CounterBuilder() {
        this.changeCounterField = new HashMap<>();
    }

    public CounterBuilder incrementField(String fieldName, int value) {
        changeCounterField.put(fieldName, Math.abs(value));
        return this;
    }

    public CounterBuilder decrementField(String fieldName, int value) {
        changeCounterField.put(fieldName, -Math.abs(value));
        return this;
    }


    public JsonObject buildQuery() {
        if (changeCounterField.isEmpty()) {
            throw new RuntimeException("Cannot create counter builder without fields to increment/decrement!");
        }
        JsonObject query = new JsonObject();
        for (Map.Entry<String, Integer> entry : changeCounterField.entrySet()) {
            JsonObject jsonIncrementer = new JsonObject();
            jsonIncrementer.addProperty(SYNCANO_INCREMENT_OR_DECREMENT, entry.getValue());
            query.add(entry.getKey(), jsonIncrementer);
        }
        return query;
    }

}
