package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;

public class Classes extends SyncanoApplicationTestCase {

    public void testCreateClass() {

        String className = "testsyncanoclass";
        JsonArray schema = TestSyncanoClass.getSchema();
        syncano.deleteSyncanoClass(className).send();

        // ---------- Creating a Class
        SyncanoClass syncanoClass = new SyncanoClass(className, schema);
        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }
}
