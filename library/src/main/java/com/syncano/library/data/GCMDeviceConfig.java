package com.syncano.library.data;

import com.google.gson.JsonObject;
import com.syncano.library.annotation.SyncanoField;

public abstract class GCMDeviceConfig {

    public static final String FIELD_LABEL = "label";
    public static final String FIELD_USER_ID = "user_id";
    public static final String FIELD_REGISTRATION_ID = "registration_id";
    public static final String FIELD_DEVICE_ID = "device_id";
    public static final String FIELD_METADATA = "metadata";
    public static final String FIELD_IS_ACTIVE = "is_active";

    @SyncanoField(name = FIELD_LABEL)
    private String label;
    @SyncanoField(name = FIELD_USER_ID)
    private Integer userId;
    @SyncanoField(name = FIELD_REGISTRATION_ID)
    private String registrationId;
    @SyncanoField(name = FIELD_DEVICE_ID)
    private String deviceId;
    @SyncanoField(name = FIELD_METADATA)
    private JsonObject metadata;
    @SyncanoField(name = FIELD_IS_ACTIVE)
    private Boolean isActive;

    public GCMDeviceConfig() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public JsonObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonObject metadata) {
        this.metadata = metadata;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
