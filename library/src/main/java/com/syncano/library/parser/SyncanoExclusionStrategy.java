package com.syncano.library.parser;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoFile;

public class SyncanoExclusionStrategy implements ExclusionStrategy {

    private boolean serializeReadOnlyFields;

    public SyncanoExclusionStrategy(boolean serializeReadOnlyFields) {
        this.serializeReadOnlyFields = serializeReadOnlyFields;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);
        // Don't serialize read only fields (like "id" or "created_at").
        // We want only to receive it, not send.
        // SyncanoFile is handled in SendRequest
        if (syncanoField == null ||
                (!serializeReadOnlyFields && syncanoField.readOnly() && !syncanoField.required()) ||
                f.getDeclaringClass().isAssignableFrom(SyncanoFile.class)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
