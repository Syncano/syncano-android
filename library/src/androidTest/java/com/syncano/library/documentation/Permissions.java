package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SyncanoClassPermissions;
import com.syncano.library.data.Group;
import com.syncano.library.data.SyncanoClass;

public class Permissions extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano.deleteSyncanoClass("class_name").send();
    }

    public void testCreateObjectPermission() {
        // ---------- PERMISSION TYPES FOR DATA OBJECTS
        // no example
        // -----------------------------
    }

    public void testClassPermisions() {
        JsonArray schema = TestSyncanoClass.getSchema();

        // ---------- PERMISSION TYPES FOR CLASSES
        SyncanoClass syncanoClass = new SyncanoClass("class_name", schema);
        syncanoClass.setOtherPermissions(SyncanoClassPermissions.CREATE_OBJECTS);

        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testClassGroupPermisions() {
        JsonArray schema = TestSyncanoClass.getSchema();
        String groupName = "group";

        Groups.deleteGroup(syncano, groupName);
        Response<Group> responseGroup = syncano.createGroup(new Group(groupName)).send();
        assertEquals(Response.HTTP_CODE_CREATED, responseGroup.getHttpResultCode());
        Group group = responseGroup.getData();
        assertNotNull(group);

        // ---------- Next, when creating a Class, you'd set group_permissions to
        // create_objects and pass a group id to the group parameter
        SyncanoClass syncanoClass = new SyncanoClass("class_name", schema);
        syncanoClass.setGroup(group.getId());
        syncanoClass.setGroupPermissions(SyncanoClassPermissions.CREATE_OBJECTS);

        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }
}
