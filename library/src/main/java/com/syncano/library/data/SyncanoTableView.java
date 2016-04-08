package com.syncano.library.data;

@Deprecated
public class SyncanoTableView extends DataEndpoint {

    public SyncanoTableView(String name, Class<? extends SyncanoObject> clazz) {
        super(name, clazz);
    }
}
