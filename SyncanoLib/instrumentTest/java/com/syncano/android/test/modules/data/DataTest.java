package com.syncano.android.test.modules.data;

import android.test.AndroidTestCase;

import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.data.ParamsDataAddChild;
import com.syncano.android.lib.modules.data.ParamsDataAddParent;
import com.syncano.android.lib.modules.data.ParamsDataCount;
import com.syncano.android.lib.modules.data.ParamsDataDelete;
import com.syncano.android.lib.modules.data.ParamsDataGet;
import com.syncano.android.lib.modules.data.ParamsDataGetOne;
import com.syncano.android.lib.modules.data.ParamsDataMove;
import com.syncano.android.lib.modules.data.ParamsDataNew;
import com.syncano.android.lib.modules.data.ParamsDataRemoveChild;
import com.syncano.android.lib.modules.data.ParamsDataRemoveParent;
import com.syncano.android.lib.modules.data.ParamsDataUpdate;
import com.syncano.android.lib.modules.data.ResponseDataCount;
import com.syncano.android.lib.modules.data.ResponseDataGet;
import com.syncano.android.lib.modules.data.ResponseDataGetOne;
import com.syncano.android.lib.modules.data.ResponseDataNew;
import com.syncano.android.lib.modules.data.ResponseDataUpdate;
import com.syncano.android.lib.modules.data.WhereFilter;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.objects.Data;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class DataTest extends AndroidTestCase {

    private static final String DATA_TEXT = "Data test text.";
    private static final String UPDATED_TEXT = "Update data test text";
    private static final String DATA_TITLE = "Data title";
    private static final String ADDITIONAL_KEY = "my additional";
    private static final String ADDITIONAL_VALUE = "my value";
    private static final Integer DATA1_VALUE = 1;
    private static final Integer DATA2_VALUE = 2;
    private static final Integer DATA3_VALUE = 3;
    private static final Integer INCREMENT_DATA_BY = 5;

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
        String dataIdOne;
        String dataIdTwo;

        // Data New
        ParamsDataNew paramsDataNew = new ParamsDataNew(testProjectId, testCollectionId, null, Data.PENDING);
        paramsDataNew.setText(DATA_TEXT);
        paramsDataNew.setTitle(DATA_TITLE);
        paramsDataNew.addParam(ADDITIONAL_KEY, ADDITIONAL_VALUE);
        paramsDataNew.setData1(DATA1_VALUE);
        paramsDataNew.setData2(DATA2_VALUE);
        paramsDataNew.setData3(DATA3_VALUE);
        ResponseDataNew responseDataNew =  syncano.dataNew(paramsDataNew);

        assertEquals(Response.CODE_SUCCESS, (int) responseDataNew.getResultCode());
        assertNotNull(responseDataNew.getData().getId());
        assertNotNull(responseDataNew.getData().getCreatedAt());
        assertNotNull(responseDataNew.getData().getUpdatedAt());
        assertEquals(Data.PENDING, responseDataNew.getData().getState());
        assertEquals(DATA_TEXT, responseDataNew.getData().getText());
        assertEquals(DATA_TITLE, responseDataNew.getData().getTitle());
        assertEquals(ADDITIONAL_VALUE, responseDataNew.getData().getAdditional().get(ADDITIONAL_KEY));
        assertEquals(DATA1_VALUE, responseDataNew.getData().getData1());
        assertEquals(DATA2_VALUE, responseDataNew.getData().getData2());
        assertEquals(DATA3_VALUE, responseDataNew.getData().getData3());
        dataIdOne = responseDataNew.getData().getId();

        ResponseDataNew responseDataNewTwo =  syncano.dataNew(paramsDataNew);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataNewTwo.getResultCode());
        dataIdTwo = responseDataNewTwo.getData().getId();

        // Data Get
        ParamsDataGet paramsDataGet = new ParamsDataGet(testProjectId, testCollectionId, null);
        ResponseDataGet responseDataGet = syncano.dataGet(paramsDataGet);

        assertEquals(Response.CODE_SUCCESS, (int) responseDataGet.getResultCode());
        assertTrue(responseDataGet.getData().length > 0);
        for (int i = 0; i < responseDataGet.getData().length ; i++) {
            assertNotNull(responseDataGet.getData()[i].getId());
            assertNotNull(responseDataGet.getData()[i].getCreatedAt());
            assertNotNull(responseDataGet.getData()[i].getState());
        }

        // Data Get One
        ParamsDataGetOne paramsDataGetOne = new ParamsDataGetOne(testProjectId, testCollectionId, null);
        paramsDataGetOne.setDataId(dataIdOne);
        ResponseDataGetOne responseDataGetOne = syncano.dataGetOne(paramsDataGetOne);

        assertEquals(Response.CODE_SUCCESS, (int) responseDataGetOne.getResultCode());
        assertNotNull(responseDataGetOne.getData().getId());
        assertNotNull(responseDataGetOne.getData().getCreatedAt());
        assertEquals(Data.PENDING, responseDataGetOne.getData().getState());
        assertEquals(DATA_TEXT, responseDataGetOne.getData().getText());

        // Data Update
        ParamsDataUpdate paramsDataUpdate = new ParamsDataUpdate(testProjectId, testCollectionId, null, dataIdOne, null);
        paramsDataUpdate.setText(UPDATED_TEXT);
        paramsDataUpdate.setIncrement(Data.AdditionalData.DATA1, INCREMENT_DATA_BY );
        paramsDataUpdate.setDecrement(Data.AdditionalData.DATA2, INCREMENT_DATA_BY );
        paramsDataUpdate.setData3(DATA1_VALUE);
        ResponseDataUpdate responseDataUpdate = syncano.dataUpdate(paramsDataUpdate);

        assertEquals(Response.CODE_SUCCESS, (int) responseDataUpdate.getResultCode());
        assertNotNull(responseDataUpdate.getData().getId());
        assertNotNull(responseDataUpdate.getData().getCreatedAt());
        assertNotNull(responseDataUpdate.getData().getUpdatedAt());
        assertEquals(Data.PENDING, responseDataUpdate.getData().getState());
        assertEquals(UPDATED_TEXT, responseDataUpdate.getData().getText());
        assertEquals((Integer)(DATA1_VALUE + INCREMENT_DATA_BY), responseDataUpdate.getData().getData1());
        assertEquals((Integer)(DATA2_VALUE - INCREMENT_DATA_BY), responseDataUpdate.getData().getData2());
        assertEquals(DATA1_VALUE, responseDataUpdate.getData().getData3());

        // Data Move
        ParamsDataMove paramsDataMove = new ParamsDataMove(testProjectId, testCollectionId, null);
        paramsDataMove.setDataIds(new String[] {dataIdOne});
        paramsDataMove.setNewState(Data.MODERATED);
        Response responseDataMove = syncano.dataMove(paramsDataMove);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataMove.getResultCode());

        // Data Copy

       /* TODO Check why error appears - TypeError: unsupported operand type(s) for -: 'list' and 'int'

        ParamsDataCopy paramsDataCopy = new ParamsDataCopy(testProjectId, testCollectionId, null, new String[]{dataIdOne});
        ResponseDataCopy responseDataCopy = syncano.dataCopy(paramsDataCopy);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataCopy.getResultCode());
        //copiedDataId = responseDataCopy.getData().getId();
        */

        // Data Add Parent
        ParamsDataAddParent paramsDataAddParent = new ParamsDataAddParent(testProjectId, testCollectionId, null, dataIdTwo, dataIdOne);
        paramsDataAddParent.setRemoveOther(false);
        Response responseDataAddParent = syncano.dataAddParent(paramsDataAddParent);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataAddParent.getResultCode());

        // Data Remove Parent
        ParamsDataRemoveParent paramsDataRemoveParent = new ParamsDataRemoveParent(testProjectId, testCollectionId, null, dataIdTwo);
        Response responseDataRemoveParent = syncano.dataRemoveParent(paramsDataRemoveParent);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataRemoveParent.getResultCode());

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

    public void testDataWhereFilter() {
        ParamsDataNew paramsDataNew1 = new ParamsDataNew(testProjectId, testCollectionId, null, Data.PENDING);
        paramsDataNew1.setData1(1);
        paramsDataNew1.setData2(2);
        paramsDataNew1.setData3(3);
        syncano.dataNew(paramsDataNew1);

        ParamsDataNew paramsDataNew2 = new ParamsDataNew(testProjectId, testCollectionId, null, Data.PENDING);
        paramsDataNew2.setData2(3);
        syncano.dataNew(paramsDataNew2);

        ParamsDataNew paramsDataNew3 = new ParamsDataNew(testProjectId, testCollectionId, null, Data.PENDING);
        paramsDataNew3.setData3(4);
        syncano.dataNew(paramsDataNew3);

        ParamsDataNew paramsDataNew4 = new ParamsDataNew(testProjectId, testCollectionId, null, Data.PENDING);
        paramsDataNew4.setData3(5);
        syncano.dataNew(paramsDataNew4);

        ParamsDataGet paramsDataGet1 = new ParamsDataGet(testProjectId, testCollectionId, null);
        paramsDataGet1.addWhereFilterParam(WhereFilter.DATA1_EQ, 1);
        ResponseDataGet responseDataGet1 = syncano.dataGet(paramsDataGet1);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataGet1.getResultCode());
        assertEquals(1, responseDataGet1.getData().length);

        ParamsDataGet paramsDataGet2 = new ParamsDataGet(testProjectId, testCollectionId, null);
        paramsDataGet2.addWhereFilterParam(WhereFilter.DATA2_GTE, 2);
        paramsDataGet2.addWhereFilterParam(WhereFilter.DATA2_LT, 4);
        ResponseDataGet responseDataGet2 = syncano.dataGet(paramsDataGet2);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataGet2.getResultCode());
        assertEquals(2, responseDataGet2.getData().length);

        ParamsDataGet paramsDataGet3 = new ParamsDataGet(testProjectId, testCollectionId, null);
        paramsDataGet3.addWhereFilterParam(WhereFilter.DATA3_NEQ, 5);
        ResponseDataGet responseDataGet3 = syncano.dataGet(paramsDataGet3);
        assertEquals(Response.CODE_SUCCESS, (int) responseDataGet3.getResultCode());
        assertEquals(3, responseDataGet3.getData().length);
    }
}
