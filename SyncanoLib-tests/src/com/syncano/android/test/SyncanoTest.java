
package com.syncano.android.test;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.data.ParamsDataDelete;
import com.syncano.android.lib.modules.data.ParamsDataGet;
import com.syncano.android.lib.modules.data.ParamsDataNew;
import com.syncano.android.lib.modules.data.ResponseDataGet;
import com.syncano.android.lib.modules.data.ResponseDataNew;

import android.test.InstrumentationTestCase;

public class SyncanoTest extends InstrumentationTestCase {

    public final static String TAG = SyncanoTest.class.getSimpleName();
    private Syncano syncano;

    @Override
    protected void setUp() throws Exception {
        syncano = new Syncano(getInstrumentation().getContext(), Constants.INSTANCE_NAME, Constants.API_KEY);
    }

    public void testDataSimpleOperations() {
        String param = "my param";
        String val = "my value";
        // create new object with text content
        ParamsDataNew newObject = new ParamsDataNew(Constants.PROJECT_ID, Constants.COLLECTION_ID, null, "Moderated");
        String text = "Test content";
        newObject.setText(text);
        newObject.addParam(param, val);
        ResponseDataNew responseNew = syncano.dataNew(newObject);
        assertTrue(responseNew.getResultCode() == Response.CODE_SUCCESS);

        // download created object
        String createdObjectId = responseNew.getData().getId();
        ParamsDataGet getObject = new ParamsDataGet(Constants.PROJECT_ID, Constants.COLLECTION_ID, null);
        getObject.setDataIds(new String[] {
            createdObjectId
        });
        ResponseDataGet responseGet = syncano.dataGet(getObject);
        assertTrue(val.equals(responseGet.getData()[0].getAdditional().get(param)));
        assertTrue(responseGet.getResultCode() == Response.CODE_SUCCESS);
        assertTrue(text.equals(responseGet.getData()[0].getText()));

        // remove object
        ParamsDataDelete delete = new ParamsDataDelete(Constants.PROJECT_ID, Constants.COLLECTION_ID, null);
        delete.setDataIds(new String[] {
            createdObjectId
        });
        Response responseDelete = syncano.dataDelete(delete);
        assertTrue(responseDelete.getResultCode() == Response.CODE_SUCCESS);
    }
}
