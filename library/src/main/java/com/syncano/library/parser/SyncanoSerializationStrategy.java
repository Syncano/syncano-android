package com.syncano.library.parser;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoFile;

class SyncanoSerializationStrategy implements ExclusionStrategy {

    private boolean readOnlyNotImportant = false;

    public SyncanoSerializationStrategy(boolean readOnlyNotImportant) {
        this.readOnlyNotImportant = readOnlyNotImportant;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {

        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);

        // Don't serialize read only fields (like "id" or "created_at").
        // We want only to receive it, not send.
        // don't serialize more complicated data structures as SyncanoFile
        if (syncanoField == null ||
                (!readOnlyNotImportant && syncanoField.readOnly() && !syncanoField.required()) ||
                f.getDeclaredClass().isAssignableFrom(SyncanoFile.class)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
