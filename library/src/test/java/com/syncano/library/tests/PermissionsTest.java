package com.syncano.library.tests;

import com.syncano.library.BuildConfig;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PermissionsTest extends SyncanoApplicationTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String GROUP_NAME = "mastersofdisasters";
    private Syncano userSyncano;

    @Before
    public void setUp() throws Exception {
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
        removeClass(Something.class);

        userSyncano = new Syncano(BuildConfig.API_KEY_USERS, BuildConfig.INSTANCE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGroupPermissionsOnClass() {
        // register user
        User newUser = new User(USER_NAME, PASSWORD);
        Response<User> response = userSyncano.registerUser(newUser).send();
        assertTrue(response.isSuccess());
        User user = response.getData();
        assertNotNull(user);

        Response<User> responseLogin = userSyncano.loginUser(USER_NAME, PASSWORD).send();
        assertTrue(responseLogin.isSuccess());

        // create new group
        Group newGroup = new Group();
        newGroup.setLabel(GROUP_NAME);
        Response<Group> groupResp = syncano.createGroup(newGroup).send();
        assertTrue(groupResp.isSuccess());
        Group group = groupResp.getData();

        // create new class
        SyncanoClass newClazz = new SyncanoClass(Something.class);
        newClazz.setGroup(group.getId());
        newClazz.setGroupPermissions(SyncanoClassPermissions.CREATE_OBJECTS);
        newClazz.setOtherPermissions(SyncanoClassPermissions.NONE);
        Response<SyncanoClass> classResp = syncano.createSyncanoClass(newClazz).send();
        assertTrue(classResp.isSuccess());

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
        assertTrue(respUserGroup.isSuccess());

        // create object
        Response<Something> respAgainCreateObj = userSyncano.createObject(newSom).send();
        assertTrue(respAgainCreateObj.isSuccess());

        // change permission to read
        clazz.setGroupPermissions(SyncanoClassPermissions.READ);
        Response<SyncanoClass> updateResp = syncano.updateSyncanoClass(clazz).send();
        assertTrue(updateResp.isSuccess());

        // create object with read permission
        Response<Something> respReadCreateObj = userSyncano.createObject(newSom).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respReadCreateObj.getHttpResultCode());

        // get objects
        Response<List<Something>> respGetObjRead = userSyncano.getObjects(Something.class).send();
        assertTrue(respGetObjRead.isSuccess());
        List<Something> soms = respGetObjRead.getData();
        assertNotNull(soms);
        assertTrue(soms.size() > 0);

        // change permission to none
        clazz.setGroupPermissions(SyncanoClassPermissions.NONE);
        Response<SyncanoClass> updateRespNone = syncano.updateSyncanoClass(clazz).send();
        assertTrue(updateRespNone.isSuccess());

        // get objects after removed permission
        Response<List<Something>> respGetObjNone = userSyncano.getObjects(Something.class).send();
        assertEquals(Response.HTTP_CODE_FORBIDDEN, respGetObjNone.getHttpResultCode());
    }

    @Test
    public void testUserPermissionOnObject() {
        // register user
        User newUser = new User(USER_NAME, PASSWORD);
        Response<User> response = userSyncano.registerUser(newUser).send();
        assertTrue(response.isSuccess());
        User user = response.getData();
        assertNotNull(user);

        Response<User> responseLogin = userSyncano.loginUser(USER_NAME, PASSWORD).send();
        assertTrue(responseLogin.isSuccess());

        // create new class
        SyncanoClass newClazz = new SyncanoClass(Something.class);
        Response<SyncanoClass> classResp = syncano.createSyncanoClass(newClazz).send();
        assertTrue(classResp.isSuccess());

        // create object
        Something newSom = new Something();
        newSom.someText = "some text";
        newSom.setOwnerPermissions(DataObjectPermissions.WRITE);
        newSom.setOtherPermissions(DataObjectPermissions.NONE);
        Response<Something> respCreateObj = syncano.createObject(newSom).send();
        assertTrue(respCreateObj.isSuccess());
        Something som = respCreateObj.getData();
        assertNotNull(som);

        // get object without permission
        Response<List<Something>> respGetObj = userSyncano.getObjects(Something.class).send();
        assertTrue(respGetObj.isSuccess());
        List<Something> soms = respGetObj.getData();
        assertNotNull(soms);
        assertTrue(soms.size() == 0);

        // give permission
        som.setOwner(user.getId());
        Response<Something> respUpdateObj = syncano.updateObject(som).send();
        assertTrue(respUpdateObj.isSuccess());

        // get objects
        Response<List<Something>> respGetObjWithPerm = userSyncano.getObjects(Something.class).send();
        assertTrue(respGetObjWithPerm.isSuccess());
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
