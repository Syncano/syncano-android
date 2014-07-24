package com.syncano.android.test.modules.administrators;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.administrators.ParamsAdminGet;
import com.syncano.android.lib.modules.administrators.ParamsAdminGetOne;
import com.syncano.android.lib.modules.administrators.ResponseAdminGet;
import com.syncano.android.lib.modules.administrators.ResponseAdminGetOne;
import com.syncano.android.lib.modules.roles.ParamsRoleGet;
import com.syncano.android.lib.modules.roles.ResponseRoleGet;
import com.syncano.android.lib.objects.Admin;
import com.syncano.android.lib.objects.Role;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class AdministratorsTest extends AndroidTestCase {

    private static final String NEW_ADMIN_EMAIL = "newadmin@syncanotest.com";
    private static final String INVITATION_MESSAGE = "New invitation message.";
    private static final String ROLE_NAME_VIEWER = "Viewer";

    private Syncano syncano = null;
    private Role role;
    private Admin admin;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));

        ParamsRoleGet paramsRoleGet = new ParamsRoleGet();
        ResponseRoleGet responseRoleGet = syncano.roleGet(paramsRoleGet);
        assertEquals(Response.CODE_SUCCESS, (int) responseRoleGet.getResultCode());

        for (int i = 0; i < responseRoleGet.getRoles().length; i++) {
            if (ROLE_NAME_VIEWER.equals(responseRoleGet.getRoles()[i].getName())) {
                role = responseRoleGet.getRoles()[i];
                return;
            }
        }

        assertTrue("Viewer role not found on list.", role != null);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAdministrators() {

        //Admin New
        /* TODO Requires verification. For now Admins and Invitations are same thing. It makes it difficult to delete it.

        ParamsAdminNew paramsAdminNew = new ParamsAdminNew(NEW_ADMIN_EMAIL, role.getId(), INVITATION_MESSAGE);
        Response responseAdminNew = syncano.adminNew(paramsAdminNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseAdminNew.getResultCode());*/

        // Admin Get
        ParamsAdminGet paramsAdminGet = new ParamsAdminGet();
        ResponseAdminGet responseAdminGet = syncano.adminGet(paramsAdminGet);

        assertEquals(Response.CODE_SUCCESS, (int) responseAdminGet.getResultCode());
        assertTrue(responseAdminGet.getAdmin().length > 0); // There should be at least one Admin.

        for (int i = 0; i < responseAdminGet.getAdmin().length ; i++) {
            assertNotNull(responseAdminGet.getAdmin()[i].getEmail());
            assertNotNull(responseAdminGet.getAdmin()[i].getRole());
        }
        admin = responseAdminGet.getAdmin()[0];

        // Admin Get One
        ParamsAdminGetOne paramsAdminGetOne = new ParamsAdminGetOne(null, admin.getId());
        ResponseAdminGetOne responseAdminGetOne = syncano.adminGetOne(paramsAdminGetOne);

        assertEquals(Response.CODE_SUCCESS, (int) responseAdminGetOne.getResultCode());
        assertNotNull(responseAdminGetOne.getAdmin().getEmail());
        assertNotNull(responseAdminGetOne.getAdmin().getRole());

        // Admin Update
        /* TODO Can't test until we can create test Admin.

        ParamsAdminUpdate paramsAdminUpdate = new ParamsAdminUpdate(admin.getId(), null, null);
        paramsAdminUpdate.setRoleId(...);
        ResponseAdminUpdate responseAdminUpdate = syncano.adminUpdate(paramsAdminUpdate);*/

        // Admin Delete
        /* TODO Admins and Invitations are different thing. It makes it difficult to test it.
        Invitation must be confirmed by mail before it becomes Admin.

        ParamsAdminDelete paramsAdminDelete = new ParamsAdminDelete(NEW_ADMIN_EMAIL, null);
        Response responseAdminDelete = syncano.adminDelete(paramsAdminDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseAdminDelete.getResultCode());*/
    }
}
