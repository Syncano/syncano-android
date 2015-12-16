package com.syncano.library.api;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public final class IncrementBuilder {
    private final static String SYNCANO_INCREMENT_OR_DECREMENT = "_increment";

    private HashMap<String, Integer> additionField = new HashMap<>();

    public IncrementBuilder increment(String fieldName, int value) {
        additionField.put(fieldName, value);
        return this;
    }

    public IncrementBuilder decrement(String fieldName, int value) {
        additionField.put(fieldName, -value);
        return this;
    }

    public boolean isAdditionFields() {
        return additionField.isEmpty();
    }

    public void build(JsonObject json) {
        for (Map.Entry<String, Integer> entry : additionField.entrySet()) {
            JsonObject jsonIncrementer = new JsonObject();
            jsonIncrementer.addProperty(SYNCANO_INCREMENT_OR_DECREMENT, entry.getValue());
            json.add(entry.getKey(), jsonIncrementer);
        }
    }
}
