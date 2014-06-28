package com.syncano.android.test.modules.data;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.data.ParamsDataAddChild;
import com.syncano.android.lib.modules.data.ParamsDataCount;
import com.syncano.android.lib.modules.data.ParamsDataDelete;
import com.syncano.android.lib.modules.data.ParamsDataGet;
import com.syncano.android.lib.modules.data.ParamsDataGetOne;
import com.syncano.android.lib.modules.data.ParamsDataMove;
import com.syncano.android.lib.modules.data.ParamsDataNew;
import com.syncano.android.lib.modules.data.ParamsDataRemoveChild;
import com.syncano.android.lib.modules.data.ParamsDataUpdate;
import com.syncano.android.lib.modules.data.ResponseDataCount;
import com.syncano.android.lib.modules.data.ResponseDataGet;
import com.syncano.android.lib.modules.data.ResponseDataGetOne;
import com.syncano.android.lib.modules.data.ResponseDataNew;
import com.syncano.android.lib.modules.data.ResponseDataUpdate;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.objects.Data;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.Config.Constants;

public class DataTest extends AndroidTestCase {

    private Syncano syncano = null;

    private String testProjectId = null;
    private String testCollectionId = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));

        // Create test project
        ParamsProjectNew paramsProjectNew = new ParamsProjectNew("CI Test Project");
        ResponseProjectNew responseProjectNew = syncano.projectNew(paramsProjectNew);
        assertEquals("Failed to create test project", Response.CODE_SUCCESS, (int) responseProjectNew.getResultCode());
        testProjectId = responseProjectNew.getProject().getId();

        // Create test collection
        ParamsCollectionNew paramsCollectionNew = new ParamsCollectionNew(testProjectId, "CI Test Collection");
        ResponseCollectionNew responseCollectionNew = syncano.collectionNew(paramsCollectionNew);
        assertEquals("Failed to create test collection", Response.CODE_SUCCESS, (int) responseCollectionNew.getResultCode());
        testCollectionId = responseCollectionNew.getCollection().getId();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        // Clean test collection (called before project is deleted)
        ParamsCollectionDelete paramsCollectionDelete = new ParamsCollectionDelete(testProjectId, testCollectionId);
        Response responseCollectionDelete = syncano.collectionDelete(paramsCollectionDelete);
        assertEquals("Failed to clean test collection", Response.CODE_SUCCESS, (int)responseCollectionDelete.getResultCode());

        // Clean test project
        ParamsProjectDelete paramsProjectDelete = new ParamsProjectDelete(testProjectId);
        Response responseProjectDelete = syncano.projectDelete(paramsProjectDelete);
        assertEquals("Failed to clean test project", Response.CODE_SUCCESS, (int) responseProjectDelete.getResultCode());
    }

    public void testData() {

        // Parameters
        String text = "Data test text.";
        String updateText = "Update data test text";
        String dataIdOne;
        String dataIdTwo;

        // Data New
        ParamsDataNew paramsDataNew = new ParamsDataNew(testProjectId, testCollectionId, null, Data.PENDING);
        paramsDataNew.setText(text);
        ResponseDataNew responseDataNew =  syncano.dataNew(paramsDataNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataNew.getResultCode());
        dataIdOne = responseDataNew.getData().getId();

        ResponseDataNew responseDataNewTwo =  syncano.dataNew(paramsDataNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataNewTwo.getResultCode());
        dataIdTwo = responseDataNewTwo.getData().getId();

        // Data Get
        ParamsDataGet paramsDataGet = new ParamsDataGet(testProjectId, testCollectionId, null);
        ResponseDataGet responseDataGet = syncano.dataGet(paramsDataGet);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataGet.getResultCode());
        assertTrue(responseDataGet.getData().length > 0);

        // Data Get One
        ParamsDataGetOne paramsDataGetOne = new ParamsDataGetOne(testProjectId, testCollectionId, null);
        paramsDataGetOne.setDataId(dataIdOne);
        ResponseDataGetOne responseDataGetOne = syncano.dataGetOne(paramsDataGetOne);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataGetOne.getResultCode());
        assertNotNull(responseDataGetOne.getData());
        assertEquals(text, responseDataGetOne.getData().getText());

        // Data Update
        ParamsDataUpdate paramsDataUpdate = new ParamsDataUpdate(testProjectId, testCollectionId, null, dataIdOne, null);
        paramsDataUpdate.setText(updateText);
        ResponseDataUpdate responseDataUpdate = syncano.dataUpdate(paramsDataUpdate);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataUpdate.getResultCode());

        // Data Move
        ParamsDataMove paramsDataMove = new ParamsDataMove(testProjectId, testCollectionId, null);
        paramsDataMove.setDataIds(new String[] {dataIdOne});
        paramsDataMove.setNewState(Data.MODERATED);
        Response responseDataMove = syncano.dataMove(paramsDataMove);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataMove.getResultCode());

        // Data Copy

       /* TODO Check why error appears - TypeError: unsupported operand type(s) for -: 'list' and 'int'

        ParamsDataCopy paramsDataCopy = new ParamsDataCopy(testProjectId, testCollectionId, null, new String[]{dataId});
        ResponseDataCopy responseDataCopy = syncano.dataCopy(paramsDataCopy);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataCopy.getResultCode());
        copiedDataId = responseDataCopy.getData().getId();*/

        // Data Add Parent
        /* TODO TypeError: delete_data_relation() got an unexpected keyword argument 'read'

        ParamsDataAddParent paramsDataAddParent = new ParamsDataAddParent(testProjectId, testCollectionId, null, dataIdTwo, dataIdOne);
        paramsDataAddParent.setRemoveOther(true);
        Response responseDataAddParent = syncano.dataAddParent(paramsDataAddParent);
        Log.e("syncano", responseDataAddParent.getError());
        assertEquals(Response.CODE_SUCCESS, (int) responseDataAddParent.getResultCode());
        */

        // Data Remove Parent
        /* TODO Check if works good when dataAddParent() will be fixed

        ParamsDataRemoveParent paramsDataRemoveParent = new ParamsDataRemoveParent(testProjectId, testCollectionId, null, dataIdTwo);
        Response responseDataRemoveParent = syncano.dataRemoveParent(paramsDataRemoveParent);
        Log.e("syncano", responseDataRemoveParent.getError());
        assertEquals(Response.CODE_SUCCESS, (int) responseDataRemoveParent.getResultCode());
        */

        // Data Add Child
        ParamsDataAddChild paramsDataAddChild = new ParamsDataAddChild(testProjectId, testCollectionId, null, dataIdOne, dataIdTwo);
        Response responseDataAddChild = syncano.dataAddChild(paramsDataAddChild);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataAddChild.getResultCode());

        // Data Remove Child
        ParamsDataRemoveChild paramsDataRemoveChild = new ParamsDataRemoveChild(testProjectId, testCollectionId, null, dataIdOne, dataIdTwo);
        Response responseDataRemoveChild = syncano.dataRemoveChild(paramsDataRemoveChild);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataRemoveChild.getResultCode());

        // Data Count
        ParamsDataCount paramsDataCount = new ParamsDataCount(testProjectId, testCollectionId, null);
        ResponseDataCount responseDataCount = syncano.dataCount(paramsDataCount);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataCount.getResultCode());
        assertTrue(responseDataCount.getCount() > 0);


        // Data Delete
        ParamsDataDelete paramsDataDelete = new ParamsDataDelete(testProjectId, testCollectionId, null);
        paramsDataDelete.setDataIds(new String[] {dataIdOne, dataIdTwo});
        Response responseDataDelete = syncano.dataDelete(paramsDataDelete);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataDelete.getResultCode());
    }
}
