package com.syncano.library.parser;

import com.google.gson.FieldNamingStrategy;
import com.syncano.library.utils.SyncanoClassHelper;

import java.lang.reflect.Field;

class SyncanoFieldNamingStrategy implements FieldNamingStrategy {

    @Override
    public String translateName(Field f) {
        return SyncanoClassHelper.getFieldName(f);
    }
}