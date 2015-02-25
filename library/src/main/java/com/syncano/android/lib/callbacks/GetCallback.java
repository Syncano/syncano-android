package com.syncano.android.lib.callbacks;

import com.syncano.android.lib.data.Page;

import java.util.List;

public interface GetCallback <T> extends SyncanoCallback {
    public void succes (List<T> objects, Page page);
}
