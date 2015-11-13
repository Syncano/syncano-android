package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.Constants;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;


public class Classes extends SyncanoApplicationTestCase {

    public void testCreateClass() {

        String className = "testsyncanoclass";
        syncano.deleteSyncanoClass(className).send();

        JsonArray schema = new JsonArray();
        JsonObject param = new JsonObject();
        param.addProperty(Constants.FIELD_NAME, "topic");
        param.addProperty(Constants.FIELD_TYPE, Constants.FIELD_TYPE_TEXT);

        // ---------- Creating a Class
        SyncanoClass syncanoClass = new SyncanoClass(className, schema);
        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }
}
