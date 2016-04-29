package com.syncano.library.model;

import com.syncano.library.annotation.SyncanoClass;

@SyncanoClass(name = "all_types_object")
public class AllTypesObject extends AllTypesProfile {

    public static AllTypesObject generateObject(AllTypesObject o) {
        return (AllTypesObject) AllTypesProfile.generateObject(o);
    }
}
