package com.syncano.library.parser;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.syncano.library.annotation.SyncanoField;

class SyncanoDeserializationStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        SyncanoField syncanoField = f.getAnnotation(SyncanoField.class);
        if (syncanoField == null) {
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
