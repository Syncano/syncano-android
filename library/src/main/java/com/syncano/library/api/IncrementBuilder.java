package com.syncano.library.api;

import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.SyncanoObject;

import java.util.HashMap;
import java.util.Map;

public final class IncrementBuilder {
    private final static String SYNCANO_INCREMENT_OR_DECREMENT = "_increment";
    private final Class type;
    private final int objectId;

    private HashMap<String, Integer> additionField = new HashMap<>();
    private Syncano syncano;

    public IncrementBuilder() {
    }

    public IncrementBuilder(SyncanoObject syncanoObject) {
        if (syncanoObject.getId() == null || syncanoObject.getId() == 0) {
            throw new RuntimeException("Trying to addition object without id!");
        }
        this.type = syncanoObject.getClass();
        this.objectId = syncanoObject.getId();
        this.syncano = syncanoObject.getSyncano();

    }

    public IncrementBuilder(SyncanoObject syncanoObject, boolean updateObject) {
        this(syncanoObject);
    }

    public IncrementBuilder(Class type, int objectId) {
        this.type = type;
        this.objectId = objectId;
    }

    public IncrementBuilder setSyncanoInstance(Syncano syncano) {
        this.syncano = syncano;
        return this;
    }

    public IncrementBuilder increment(String fieldName, int value) {
        additionField.put(fieldName, value);
        return this;
    }

    public IncrementBuilder decrement(String fieldName, int value) {
        additionField.put(fieldName, -value);
        return this;
    }

    private Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }


    public JsonObject buildQuery() {
        if (additionField.isEmpty()) {
            throw new RuntimeException("Cannot addition query without fields to increment/decrement!");
        }
        JsonObject query = new JsonObject();
        for (Map.Entry<String, Integer> entry : additionField.entrySet()) {
            JsonObject jsonIncrementer = new JsonObject();
            jsonIncrementer.addProperty(SYNCANO_INCREMENT_OR_DECREMENT, entry.getValue());
            query.add(entry.getKey(), jsonIncrementer);
        }
        return query;
    }


    public <T extends SyncanoObject> Response<T> send() {
        Request<T> req = (Request<T>) getSyncano().addition(type, objectId, this);
        return req.send();
    }

    public <T extends SyncanoObject> void send(SyncanoCallback<T> callback) {
        Request<T> req = (Request<T>) getSyncano().addition(type, objectId, this);
        req.sendAsync(callback);
    }

}
