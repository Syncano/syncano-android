package com.syncano.library.simple;

import com.syncano.library.Syncano;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.Response;
import com.syncano.library.api.SendRequest;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.data.Entity;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineDeleteRequest;
import com.syncano.library.offline.OfflineFetchRequest;
import com.syncano.library.offline.OfflineMode;
import com.syncano.library.offline.OfflineRequest;
import com.syncano.library.offline.OfflineSaveRequest;

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
    private OfflineMode mode = null;
    private Boolean cleanStorageOnSuccessDownload = null;
    private Boolean saveDownloadedDataToStorage = null;
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
        OfflineSaveRequest<T> req = prepareSaveRequest(true);
        return req.send();
    }

    public <T extends SyncanoObject> void save(SyncanoCallback<T> callback) {
        OfflineSaveRequest<T> req = prepareSaveRequest(false);
        req.sendAsync(callback);
    }

    public <T extends SyncanoObject> Response<T> delete() {
        OfflineDeleteRequest<T> req = prepareDeleteRequest();
        return req.send();
    }

    public <T extends SyncanoObject> void delete(SyncanoCallback<T> callback) {
        OfflineDeleteRequest<T> req = prepareDeleteRequest();
        req.sendAsync(callback);
    }

    public <T extends SyncanoObject> Response<T> fetch() {
        OfflineFetchRequest<T> req = prepareFetchRequest();
        return req.send();
    }

    public <T extends SyncanoObject> void fetch(SyncanoCallback<T> callback) {
        OfflineFetchRequest<T> req = prepareFetchRequest();
        req.sendAsync(callback);
    }

    public void resetRequestBuildingFields() {
        incrementBuilder = new IncrementBuilder();
        fieldsToClear = new HashSet<>();
        mode = null;
        cleanStorageOnSuccessDownload = null;
        saveDownloadedDataToStorage = null;
    }

    private <T extends SyncanoObject> void decorateOfflineRequest(OfflineRequest<T> request) {
        if (mode != null) {
            request.mode(mode);
        }
        if (cleanStorageOnSuccessDownload != null) {
            request.cleanStorageOnSuccessDownload(cleanStorageOnSuccessDownload);
        }
        if (saveDownloadedDataToStorage != null) {
            request.saveDownloadedDataToStorage(saveDownloadedDataToStorage);
        }
        request.setBackgroundCallback((SyncanoCallback<T>) backgroundCallback);
    }

    private <T extends SyncanoObject> OfflineSaveRequest<T> prepareSaveRequest(boolean updateGivenObject) {
        SendRequest<T> onlineRequest;
        if (getId() == null) {
            onlineRequest = getSyncano().createObject((T) this, updateGivenObject);
        } else {
            onlineRequest = getSyncano().updateObject((T) this, updateGivenObject);
        }
        OfflineSaveRequest<T> offlineRequest = new OfflineSaveRequest<>(onlineRequest);
        decorateOfflineRequest(offlineRequest);
        return offlineRequest;
    }

    private <T extends SyncanoObject> OfflineFetchRequest<T> prepareFetchRequest() {
        OfflineFetchRequest<T> offlineRequest = new OfflineFetchRequest<>(getSyncano().getObject((T) this));
        decorateOfflineRequest(offlineRequest);
        return offlineRequest;
    }

    private <T extends SyncanoObject> OfflineDeleteRequest<T> prepareDeleteRequest() {
        OfflineDeleteRequest<T> offlineRequest = new OfflineDeleteRequest<>(getSyncano().deleteObject((T) this));
        decorateOfflineRequest(offlineRequest);
        return offlineRequest;
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
