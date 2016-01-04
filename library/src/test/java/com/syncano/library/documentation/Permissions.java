package com.syncano.library.documentation;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.choice.SyncanoClassPermissions;
import com.syncano.library.data.Group;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Permissions extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        removeClass(ExampleObject.class);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testCreateObjectPermission() throws InterruptedException {
        createClass(ExampleObject.class);
        // ---------- Creating a Data Object with an owner_permissions example
        ExampleObject obj = new ExampleObject();
        obj.setOwnerPermissions(DataObjectPermissions.READ);
        obj.data = "I am sample data";
        Response<ExampleObject> response = obj.save();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
        assertNotNull(response.getData());
        assertEquals(DataObjectPermissions.READ, response.getData().getOwnerPermissions());
    }

    @Test
    public void testClassPermisions() {
        // ---------- PERMISSION TYPES FOR CLASSES
        SyncanoClass syncanoClass = new SyncanoClass(ExampleObject.class);
        syncanoClass.setOtherPermissions(SyncanoClassPermissions.CREATE_OBJECTS);

        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    @Test
    public void testClassGroupPermisions() {
        String groupName = "group";

        Groups.deleteGroup(syncano, groupName);
        Response<Group> responseGroup = syncano.createGroup(new Group(groupName)).send();
        assertEquals(Response.HTTP_CODE_CREATED, responseGroup.getHttpResultCode());
        Group group = responseGroup.getData();
        assertNotNull(group);

        // ---------- Next, when creating a Class, you'd set group_permissions to

        SyncanoClass syncanoClass = new SyncanoClass(ExampleObject.class);
        syncanoClass.setGroup(group.getId());
        syncanoClass.setGroupPermissions(SyncanoClassPermissions.CREATE_OBJECTS);

        Response<SyncanoClass> response = syncano.createSyncanoClass(syncanoClass).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    @com.syncano.library.annotation.SyncanoClass(name = "Example")
    private static class ExampleObject extends SyncanoObject {
        @SyncanoField(name = "data")
        public String data;
    }
}
