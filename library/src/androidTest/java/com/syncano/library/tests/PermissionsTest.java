package com.syncano.library.tests;

import com.syncano.library.Config;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.choice.SyncanoClassPermissions;
import com.syncano.library.data.Group;
import com.syncano.library.data.GroupMembership;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;

import java.util.List;

public class PermissionsTest extends SyncanoApplicationTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String GROUP_NAME = "mastersofdisasters";
    private Syncano userSyncano;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // delete user
        Response<List<User>> usersResponse = syncano.getUsers().send();
        List<User> users = usersResponse.getData();
        assertNotNull(users);
        for (User u : users) {
            if (USER_NAME.equals(u.getUserName())) {
                syncano.deleteUser(u.getId()).send();
                break;
            }
        }

        // delete group
        Response<List<Group>> groupsResponse = syncano.getGroups().send();
        List<Group> groups = groupsResponse.getData();
        assertNotNull(groups);
        for (Group g : groups) {
            if (GROUP_NAME.equals(g.getLabel())) {
                syncano.deleteGroup(g.getId()).send();
                break;
            }
        }

        //delete class
        syncano.deleteSyncanoClass(Something.class).send();

        userSyncano = new Syncano(Config.API_KEY_USERS, Config.INSTANCE_NAME);
    }

    public void testGroupPermissionsOnClass() {
        // register user
        User newUser = new User(USER_NAME, PASSWORD);
        Response<User> response = userSyncano.createUser(newUser).send();
        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
        User user = response.getData();
        assertNotNull(user);

        Response<User> responseLogin = userSyncano.loginUser(USER_NAME, PASSWORD).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, responseLogin.getHttpResultCode());

        // remember user key
        userSyncano.setUserKey(responseLogin.getData().getUserKey());

        // create new group
        Group newGroup = new Group();
        newGroup.setLabel(GROUP_NAME);
        Response<Group> groupResp = syncano.createGroup(newGroup).send();
        assertEquals(Response.HTTP_CODE_CREATED, groupResp.getHttpResultCode());
        Group group = groupResp.getData();

        // create new class
        SyncanoClass newClazz = new SyncanoClass(Something.class);
        newClazz.setGroup(group.getId());
        newClazz.setGroupPermissions(SyncanoClassPermissions.CREATE_OBJECTS);
        newClazz.setOtherPermissions(SyncanoClassPermissions.NONE);
        Response<SyncanoClass> classResp = syncano.createSyncanoClass(newClazz).send();
        assertEquals(Response.HTTP_CODE_CREATED, classResp.getHttpResultCode());

        SyncanoClass clazz = classResp.getData();
        assertNotNull(clazz);
        assertEquals(SyncanoClassPermissions.CREATE_OBJECTS, clazz.getGroupPermissions());
        assertEquals(SyncanoClassPermissions.NONE, clazz.getOtherPermissions());

        // get objects without permission
        Response<List<Something>> respGetObj = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respGetObj.getHttpResultCode());

        // create object without permission
        Something newSom = new Something();
        newSom.someText = "some text";
        Response<Something> respCreateObj = userSyncano.createObject(newSom).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respCreateObj.getHttpResultCode());

        // add user to group
        Response<GroupMembership> respUserGroup = syncano.addUserToGroup(group.getId(), user.getId()).send();
        assertEquals(Response.HTTP_CODE_CREATED, respUserGroup.getHttpResultCode());

        // create object
        Response<Something> respAgainCreateObj = userSyncano.createObject(newSom).send();
        assertEquals(Response.HTTP_CODE_CREATED, respAgainCreateObj.getHttpResultCode());

        // change permission to read
        clazz.setGroupPermissions(SyncanoClassPermissions.READ);
        Response<SyncanoClass> updateResp = syncano.updateSyncanoClass(clazz).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, updateResp.getHttpResultCode());

        // create object with read permission
        Response<Something> respReadCreateObj = userSyncano.createObject(newSom).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respReadCreateObj.getHttpResultCode());

        // get objects
        Response<List<Something>> respGetObjRead = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, respGetObjRead.getHttpResultCode());
        List<Something> soms = respGetObjRead.getData();
        assertNotNull(soms);
        assertTrue(soms.size() > 0);

        // change permission to none
        clazz.setGroupPermissions(SyncanoClassPermissions.NONE);
        Response<SyncanoClass> updateRespNone = syncano.updateSyncanoClass(clazz).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, updateRespNone.getHttpResultCode());

        // get objects after removed permission
        Response<List<Something>> respGetObjNone = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respGetObjNone.getHttpResultCode());
    }

    public void testUserPermissionOnObject() {
        // register user
        User newUser = new User(USER_NAME, PASSWORD);
        Response<User> response = userSyncano.createUser(newUser).send();
        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
        User user = response.getData();
        assertNotNull(user);

        Response<User> responseLogin = userSyncano.loginUser(USER_NAME, PASSWORD).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, responseLogin.getHttpResultCode());

        // remember user key
        userSyncano.setUserKey(responseLogin.getData().getUserKey());

        // create new class
        SyncanoClass newClazz = new SyncanoClass(Something.class);
        Response<SyncanoClass> classResp = syncano.createSyncanoClass(newClazz).send();
        assertEquals(Response.HTTP_CODE_CREATED, classResp.getHttpResultCode());

        // create object
        Something newSom = new Something();
        newSom.someText = "some text";
        newSom.setOwnerPermissions(DataObjectPermissions.WRITE);
        newSom.setOtherPermissions(DataObjectPermissions.NONE);
        Response<Something> respCreateObj = syncano.createObject(newSom).send();
        assertEquals(Response.HTTP_CODE_CREATED, respCreateObj.getHttpResultCode());
        Something som = respCreateObj.getData();
        assertNotNull(som);

        // get object without permission
        Response<List<Something>> respGetObj = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, respGetObj.getHttpResultCode());
        List<Something> soms = respGetObj.getData();
        assertNotNull(soms);
        assertTrue(soms.size() == 0);

        // give permission
        som.setOwner(user.getId());
        Response<Something> respUpdateObj = syncano.updateObject(som).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, respUpdateObj.getHttpResultCode());

        // get objects
        Response<List<Something>> respGetObjWithPerm = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_SUCCESS, respGetObjWithPerm.getHttpResultCode());
        List<Something> soms2 = respGetObjWithPerm.getData();
        assertNotNull(soms2);
        assertTrue(soms2.size() > 0);
    }

    @com.syncano.library.annotation.SyncanoClass(name = "just_something")
    private static class Something extends SyncanoObject {
        @SyncanoField(name = "some_text")
        public String someText;
    }
}
