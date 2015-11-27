package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.data.Group;
import com.syncano.library.data.GroupMembership;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;

import java.util.List;

public class Groups extends SyncanoApplicationTestCase {

    public final static String GROUP_NAME = "group_label";
    public final static String USER_NAME = "user_name";
    public final static String USER_PASS = "passwordofuser";
    private Group group;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteGroup(syncano, GROUP_NAME);
        Response<Group> response = syncano.createGroup(new Group(GROUP_NAME)).send();
        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
        group = response.getData();
        assertNotNull(group);

        String className = "Book";
        syncano.deleteSyncanoClass(className).send();
        SyncanoClass clazz = new SyncanoClass(className, Book.getSchema());
        Response<SyncanoClass> classResponse = syncano.createSyncanoClass(clazz).send();
        assertEquals(Response.HTTP_CODE_CREATED, classResponse.getHttpResultCode());
    }

    public static void deleteGroup(Syncano syncano, String label) {
        Response<List<Group>> r = syncano.getGroups().send();
        List<Group> groups = r.getData();
        assertNotNull(groups);
        for (Group g : groups) {
            if (label.equals(g.getLabel())) {
                syncano.deleteGroup(g.getId()).send();
            }
        }
    }

    public void testCreateGroup() {
        deleteGroup(syncano, GROUP_NAME);

        // ---------- Creating a Group
        Group newGroup = new Group("group_label");
        Response<Group> response = syncano.createGroup(newGroup).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testAddUserToGroup() {
        UserManagement.deleteTestUser(syncano, USER_NAME);
        Response<User> createResponse = syncano.registerUser(new User(USER_NAME, USER_PASS)).send();
        assertEquals(Response.HTTP_CODE_CREATED, createResponse.getHttpResultCode());
        User user = createResponse.getData();

        // ---------- Adding Users to a Group
        Response<GroupMembership> response = syncano.addUserToGroup(group.getId(), user.getId()).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testAddObjectGroupPermission() {
        // ---------- Creating objects with Group permissions
        final Book book = new Book();
        book.title = "Title";
        book.setGroup(group.getId());
        book.setGroupPermisions(DataObjectPermissions.FULL);

        Response<Book> response = syncano.createObject(book).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }


    @com.syncano.library.annotation.SyncanoClass(name = "Book")
    private static class Book extends SyncanoObject {
        @SyncanoField(name = "author")
        public String author;

        @SyncanoField(name = "title")
        public String title;

        public static JsonArray getSchema() {
        /*
        [
            {"type": "string","name": "author"},
            {"type": "string","name": "title"}
        ]
         */

            JsonObject fieldOne = new JsonObject();
            fieldOne.addProperty("type", "string");
            fieldOne.addProperty("name", "author");

            JsonObject fieldTwo = new JsonObject();
            fieldTwo.addProperty("type", "string");
            fieldTwo.addProperty("name", "title");

            JsonArray array = new JsonArray();
            array.add(fieldOne);
            array.add(fieldTwo);

            return array;
        }
    }
}
