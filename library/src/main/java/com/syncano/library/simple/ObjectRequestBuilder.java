package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.HttpRequest;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineMode;

import java.util.HashSet;

/**
 * It could be a part of code in SyncanoObject class but this class was made to divide pure data kept
 * by SyncanoObject and logic behind building request affecting this specific object.
 * Only SyncanoObject can inherit this class.
 */
public abstract class ObjectRequestBuilder extends Entity {
    private Syncano syncano;
    private IncrementBuilder incrementBuilder = new IncrementBuilder();
    private HashSet<String> fieldsToClear = new HashSet<>();

    private OfflineMode mode = OfflineMode.ONLINE;

    public void clearField(String fieldName) {
        fieldsToClear.add(fieldName);
    }

    public void removeFromClearList(String fieldName) {
        fieldsToClear.remove(fieldName);
    }

    public boolean isOnClearList(String fieldName) {
        return fieldsToClear.contains(fieldName);
    }

    public IncrementBuilder getIncrementBuilder() {
        return incrementBuilder;
    }

    public <T extends SyncanoObject> T increment(String fieldName, int value) {
        incrementBuilder.increment(fieldName, value);
        return (T) this;
    }

    public <T extends SyncanoObject> T decrement(String fieldName, int value) {
        incrementBuilder.decrement(fieldName, value);
        return (T) this;
    }

    public Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public <T extends SyncanoObject> T on(Syncano syncano) {
        this.syncano = syncano;
        return (T) this;
    }

    public <T extends SyncanoObject> Response<T> save() {
        if (getId() == null) {
            return getSyncano().createObject((T) this, true).send();
        }
        return getSyncano().updateObject((T) this, true).send();
    }

    public <T extends SyncanoObject> void save(SyncanoCallback<T> callback) {
        if (getId() == null) {
            getSyncano().createObject((T) this, false).sendAsync(callback);
        } else {
            getSyncano().updateObject((T) this, false).sendAsync(callback);
        }
    }

    public <T extends SyncanoObject> Response<T> delete() {
        return getSyncano().deleteObject((T) this).send();
    }

    public <T extends SyncanoObject> void delete(SyncanoCallback<T> callback) {
        getSyncano().deleteObject((T) this).sendAsync(callback);
    }

    //TODO add offline here
    public <T extends SyncanoObject> Response<T> fetch() {
        HttpRequest<T> req = getSyncano().getObject((T) this);
        return req.send();
    }

    public <T extends SyncanoObject> void fetch(SyncanoCallback<T> callback) {
        HttpRequest<T> req = getSyncano().getObject((T) this);
        req.sendAsync(callback);
    }
}
