package com.syncano.library.utils;

import java.util.HashSet;

public class SyncanoHashSet extends HashSet<String> {

    public String getAsString() {
        StringBuilder stringBuilder = new StringBuilder(size());
        for (String item : this) {
            if (stringBuilder.length() != 0)
                stringBuilder.append(',');
            stringBuilder.append(item);
        }
        return stringBuilder.toString();
    }
}
