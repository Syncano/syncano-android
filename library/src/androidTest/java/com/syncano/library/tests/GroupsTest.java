package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.RequestGetList;
import com.syncano.library.api.Response;
import com.syncano.library.api.Where;
import com.syncano.library.data.Group;
import com.syncano.library.data.GroupMembership;
import com.syncano.library.data.User;

import java.util.List;

public class GroupsTest extends SyncanoApplicationTestCase {
    private static final String USER_NAME = "testuser";
    private static final String PASSWORD = "password";

    private static final String GROUP_LABEL = "group";
    private static final String NEW_GROUP_NAME = "new_group";

    private User user;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // ----------------- Get Or Create Test User -----------------
        Response<User> responseUserAuth = syncano.loginUser(USER_NAME, PASSWORD).send();
        if (responseUserAuth.isSuccess() && responseUserAuth.getData() != null) {
            user = responseUserAuth.getData();
        } else {
            Response<User> responseCreateUser = syncano.registerUser(new User(USER_NAME, PASSWORD)).send();

            assertTrue(responseCreateUser.isSuccess());
            assertNotNull(responseCreateUser.getData());
            user = responseCreateUser.getData();
        }

        // ----------------- Remove All Groups -----------------
        Where where = new Where();
        where.eq(Group.FIELD_LABEL, GROUP_LABEL);

        RequestGetList<Group> requestGetGroups = syncano.getGroups();
        requestGetGroups.setWhereFilter(where);
        Response<List<Group>> responseGetGroups = requestGetGroups.send();

        assertTrue(responseGetGroups.isSuccess());
        assertNotNull(responseGetGroups.getData());

        for (Group item : responseGetGroups.getData()) {
            Response<Group> responseDeleteGroup = syncano.deleteGroup(item.getId()).send();
            assertTrue(responseDeleteGroup.isSuccess());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // ----------------- Delete Test User -----------------
        if (user != null) {
            Response<User> responseDeleteUser = syncano.deleteUser(user.getId()).send();
            assertTrue(responseDeleteUser.isSuccess());
        }
    }


    public void testGroups() throws InterruptedException {

        final Group newGroup = new Group(GROUP_LABEL);
        Group group;

        // ----------------- Create -----------------
        Response<Group> responseCreateGroup = syncano.createGroup(newGroup).send();

        assertTrue(responseCreateGroup.isSuccess());
        assertNotNull(responseCreateGroup.getData());
        group = responseCreateGroup.getData();

        // ----------------- Get One -----------------
        Response<Group> responseGetGroup = syncano.getGroup(group.getId()).send();

        assertTrue(responseGetGroup.isSuccess());
        assertNotNull(responseGetGroup.getData());
        assertEquals(group.getLabel(), responseGetGroup.getData().getLabel());

        // ----------------- Get List -----------------
        Response<List<Group>> responseGetGroups = syncano.getGroups().send();

        assertNotNull(responseGetGroups.getData());
        assertTrue("List should contain at least one item.", responseGetGroups.getData().size() > 0);

        // ----------------- Update -----------------
        group.setLabel(NEW_GROUP_NAME);
        Response<Group> responseUpdateGroup = syncano.updateGroup(group).send();

        assertTrue(responseUpdateGroup.isSuccess());
        assertNotNull(responseUpdateGroup.getData());
        assertEquals(group.getLabel(), responseUpdateGroup.getData().getLabel());

        // ----------------- Delete -----------------
        Response<Group> responseDeleteGroup = syncano.deleteGroup(group.getId()).send();

        assertTrue(responseDeleteGroup.isSuccess());
    }

    public void testGroupMembership() throws InterruptedException {

        GroupMembership groupMembership;

        // ----------------- Create Temporary Group -----------------
        Response<Group> responseCreateGroup = syncano.createGroup(new Group(GROUP_LABEL)).send();
        assertTrue(responseCreateGroup.isSuccess());
        Group group = responseCreateGroup.getData();

        // ----------------- Add User To Group -----------------
        Response<GroupMembership> responseAddUserToGroup = syncano.addUserToGroup(group.getId(), user.getId()).send();

        assertTrue(responseAddUserToGroup.isSuccess());
        assertNotNull(responseAddUserToGroup.getData());
        groupMembership = responseAddUserToGroup.getData();
        assertNotNull(groupMembership.getUser());

        // ----------------- Get Group Membership -----------------
        Response<GroupMembership> responseGetGroupMembership = syncano.getGroupMembership(group.getId(), user.getId()).send();

        assertTrue(responseGetGroupMembership.isSuccess());
        assertNotNull(responseGetGroupMembership.getData());
        assertNotNull(responseGetGroupMembership.getData().getUser());
        assertEquals(user.getId(), responseGetGroupMembership.getData().getUser().getId());

        // ----------------- Get Group Membership List -----------------
        Response<List<GroupMembership>> responseGetGroupMemberships = syncano.getGroupMemberships(group.getId()).send();

        assertNotNull(responseGetGroupMemberships.getData());
        assertTrue("List should contain at least one item.", responseGetGroupMemberships.getData().size() > 0);

        // ----------------- Delete Group Membership -----------------
        Response<GroupMembership> responseDeleteUserFromGroup = syncano.deleteUserFromGroup(group.getId(), user.getId()).send();
        assertTrue(responseDeleteUserFromGroup.isSuccess());

        // ----------------- Delete Temporary Group -----------------
        Response<Group> responseDeleteGroup = syncano.deleteGroup(group.getId()).send();
        assertTrue(responseDeleteGroup.isSuccess());
    }
}