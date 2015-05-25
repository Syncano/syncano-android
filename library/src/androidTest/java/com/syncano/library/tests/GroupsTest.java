package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.Config;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.data.Group;
import com.syncano.library.data.Profile;
import com.syncano.library.data.User;

import java.util.List;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class GroupsTest extends ApplicationTestCase<Application> {

    private static final String TAG = GroupsTest.class.getSimpleName();

    private Syncano syncano;

    private static final String GROUP_NAME = "group";
    private static final String NEW_GROUP_NAME = "new_group";

    public GroupsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);
    }

    @Override
    protected void tearDown() throws Exception {
       super.tearDown();
    }


    public void testGroups() throws InterruptedException {

        final Group newGroup = new Group(GROUP_NAME);
        Group group;

        // ----------------- Create -----------------
        Response<Group> responseCreateGroup = syncano.createGroup(newGroup).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateGroup.getHttpResultCode());
        assertNotNull(responseCreateGroup.getData());
        group = responseCreateGroup.getData();

        // ----------------- Get One -----------------
        Response<Group> responseGetGroup = syncano.getGroup(group.getId()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetGroup.getHttpResultCode());
        assertNotNull(responseGetGroup.getData());
        assertEquals(group.getName(), responseGetGroup.getData().getName());

        // ----------------- Get List -----------------
        Response<List<Group>> responseGetGroups = syncano.getGroups().send();

        assertNotNull(responseGetGroups.getData());
        assertTrue("List should contain at least one item.", responseGetGroups.getData().size() > 0);

        // ----------------- Update -----------------
        group.setName(NEW_GROUP_NAME);
        Response<Group> responseUpdateGroup= syncano.updateGroup(group).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateGroup.getHttpResultCode());
        assertNotNull(responseUpdateGroup.getData());
        assertEquals(group.getName(), responseUpdateGroup.getData().getName());

        // ----------------- Delete -----------------
        Response<Group> responseDeleteGroup = syncano.deleteGroup(group.getId()).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteGroup.getHttpResultCode());
    }
}