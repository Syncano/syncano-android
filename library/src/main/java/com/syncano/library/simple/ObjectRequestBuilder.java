package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.RequestGet;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResultRequest;
import com.syncano.library.api.SendRequest;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineGetOneRequest;
import com.syncano.library.offline.OfflineMode;

import java.util.HashSet;

/**
 * It could be a part of code in SyncanoObject class but this class was made to divide pure data kept
 * by SyncanoObject and logic behind building request affecting this specific object.
 * Only SyncanoObject can inherit this class.
 */
public abstract class ObjectRequestBuilder extends Entity {
    private Syncano syncano;
    // TODO check how increment works in offline
    private IncrementBuilder incrementBuilder = new IncrementBuilder();
    // TODO check how clearing works in offline
    private HashSet<String> fieldsToClear = new HashSet<>();
    private OfflineMode mode = OfflineMode.ONLINE;
    private boolean cleanStorageOnSuccessDownload = false;
    private boolean saveDownloadedDataToStorage = false;
    private SyncanoCallback<? extends SyncanoObject> backgroundCallback;

    public <T extends SyncanoObject> T clearField(String fieldName) {
        fieldsToClear.add(fieldName);
        return (T) this;
    }

    public <T extends SyncanoObject> T removeFromClearList(String fieldName) {
        fieldsToClear.remove(fieldName);
        return (T) this;
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
        return (Response<T>) prepareOfflineRequest(prepareSaveRequest(true)).send();
    }

    public <T extends SyncanoObject> void save(SyncanoCallback<T> callback) {
        prepareOfflineRequest(prepareSaveRequest(false)).sendAsync((SyncanoCallback<SyncanoObject>) callback);
    }

    public <T extends SyncanoObject> Response<T> delete() {
        OfflineGetOneRequest<T> req = prepareOfflineRequest(getSyncano().deleteObject((T) this));
        return req.send();
    }

    public <T extends SyncanoObject> void delete(SyncanoCallback<T> callback) {
        prepareOfflineRequest(getSyncano().deleteObject((T) this)).sendAsync(callback);
    }

    public <T extends SyncanoObject> Response<T> fetch() {
        OfflineGetOneRequest<T> req = (OfflineGetOneRequest<T>) prepareOfflineRequest(prepareGetRequest());
        return req.send();
    }

    public <T extends SyncanoObject> void fetch(SyncanoCallback<T> callback) {
        OfflineGetOneRequest<T> req = (OfflineGetOneRequest<T>) prepareOfflineRequest(prepareGetRequest());
        req.sendAsync(callback);
    }

    public void resetRequestBuildingFields() {
        incrementBuilder = new IncrementBuilder();
        fieldsToClear = new HashSet<>();
        mode = OfflineMode.ONLINE;
        cleanStorageOnSuccessDownload = false;
        saveDownloadedDataToStorage = false;
    }

    public <T extends SyncanoObject> OfflineGetOneRequest<T> prepareOfflineRequest(ResultRequest<T> request) {
        OfflineGetOneRequest<T> offlineRequest = new OfflineGetOneRequest<>(request);
        offlineRequest.mode(mode);
        offlineRequest.cleanStorageOnSuccessDownload(cleanStorageOnSuccessDownload);
        offlineRequest.saveDownloadedDataToStorage(saveDownloadedDataToStorage);
        offlineRequest.setBackgroundCallback((SyncanoCallback<T>) backgroundCallback);
        return offlineRequest;
    }

    private <T extends SyncanoObject> SendRequest<T> prepareSaveRequest(boolean updateGivenObject) {
        if (getId() == null) {
            return getSyncano().createObject((T) this, updateGivenObject);
        }
        return getSyncano().updateObject((T) this, updateGivenObject);
    }

    private <T extends SyncanoObject> RequestGet<T> prepareGetRequest() {
        return getSyncano().getObject((T) this);
    }

    public ObjectRequestBuilder mode(OfflineMode mode) {
        this.mode = mode;
        return this;
    }

    public ObjectRequestBuilder cleanStorageOnSuccessDownload(boolean clean) {
        this.cleanStorageOnSuccessDownload = clean;
        return this;
    }

    public ObjectRequestBuilder saveDownloadedDataToStorage(boolean save) {
        this.saveDownloadedDataToStorage = save;
        return this;
    }

    public ObjectRequestBuilder backgroundCallback(SyncanoCallback<? extends SyncanoObject> callback) {
        this.backgroundCallback = callback;
        return this;
    }
}
