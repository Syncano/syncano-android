package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.Response;
import com.syncano.library.choice.SyncanoClassPermissions;
import com.syncano.library.data.Group;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.utils.SyncanoClassHelper;

public class Permissions extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano.deleteSyncanoClass(SyncanoClassHelper.getSyncanoClassName(TestSyncanoObject.class)).send();
    }

    public void testCreateObjectPermission() {
        // ---------- PERMISSION TYPES FOR DATA OBJECTS
        // no example
        // -----------------------------
    }

    public void testClassPermisions() {
        // ---------- PERMISSION TYPES FOR CLASSES
        SyncanoClass syncanoClass = new SyncanoClass(TestSyncanoObject.class);
        syncanoClass.setOtherPermissions(SyncanoClassPermissions.CREATE_OBJECTS);

        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testClassGroupPermisions() {
        String groupName = "group";

        Groups.deleteGroup(syncano, groupName);
        Response<Group> responseGroup = syncano.createGroup(new Group(groupName)).send();
        assertEquals(Response.HTTP_CODE_CREATED, responseGroup.getHttpResultCode());
        Group group = responseGroup.getData();
        assertNotNull(group);

        // ---------- Next, when creating a Class, you'd set group_permissions to
        // create_objects and pass a group id to the group parameter
        SyncanoClass syncanoClass = new SyncanoClass(TestSyncanoObject.class);
        syncanoClass.setGroup(group.getId());
        syncanoClass.setGroupPermissions(SyncanoClassPermissions.CREATE_OBJECTS);

        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }
}
