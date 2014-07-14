package com.syncano.android.test.modules.roles;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.roles.ParamsRoleGet;
import com.syncano.android.lib.modules.roles.ResponseRoleGet;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class RolesTest extends AndroidTestCase {

    private Syncano syncano = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRoles() {

        ParamsRoleGet paramsRoleGet = new ParamsRoleGet();
        ResponseRoleGet responseRoleGet = syncano.roleGet(paramsRoleGet);
        assertEquals(Response.CODE_SUCCESS, (int) responseRoleGet.getResultCode());
        assertTrue(responseRoleGet.getRoles().length > 0);

    }
}
