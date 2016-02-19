package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;

public class PushDevice {
    @SyncanoField(name = "device_id")
    public String deviceId;
    @SyncanoField(name = "is_active")
    public Boolean isActive;
    @SyncanoField(name = "label")
    public String label;
    @SyncanoField(name = "registration_id")
    public String registrationId;
}