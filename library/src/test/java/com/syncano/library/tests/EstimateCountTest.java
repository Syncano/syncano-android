package com.syncano.library.tests;

import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.data.SyncanoObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EstimateCountTest extends SyncanoApplicationTestCase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(SampleSyncanoObject.class);
        createTestsObjects();
    }

    private void createTestsObjects() {
        SampleSyncanoObject testObj1 = new SampleSyncanoObject();
        SampleSyncanoObject testObj2 = new SampleSyncanoObject();
        SampleSyncanoObject testObj3 = new SampleSyncanoObject();
        Response<SampleSyncanoObject> responseCreateArticle1 = testObj1.save();
        Response<SampleSyncanoObject> responseCreateArticle2 = testObj2.save();
        Response<SampleSyncanoObject> responseCreateArticle3 = testObj3.save();
        assertTrue(responseCreateArticle1.isSuccess());
        assertTrue(responseCreateArticle2.isSuccess());
        assertTrue(responseCreateArticle3.isSuccess());
    }

    @After
    public void tearDown() throws Exception {
        removeClass(SampleSyncanoObject.class);
        super.tearDown();
    }

    @Test
    public void testEstimateCountTest() {
        Response<Integer> estimateResponse = Syncano.please(SampleSyncanoObject.class).getCountEstimation();
        assertTrue(estimateResponse.isSuccess());
        assertEquals(Integer.valueOf(3), estimateResponse.getData());
        ResponseGetList<SampleSyncanoObject> objectsWithEstimateCountResponse = Syncano.please(SampleSyncanoObject.class).estimateCount().getAll();
        assertTrue(objectsWithEstimateCountResponse.isSuccess());
        assertEquals(Integer.valueOf(3), objectsWithEstimateCountResponse.getEstimatedCount());
        Integer accurateSize = objectsWithEstimateCountResponse.getData().size();
        assertEquals(accurateSize, objectsWithEstimateCountResponse.getEstimatedCount());
    }


    @SyncanoClass(name = SampleSyncanoObject.TABLE_NAME)
    private static class SampleSyncanoObject extends SyncanoObject {
        public final static String TABLE_NAME = "sample_count_object";

        public SampleSyncanoObject() {

        }
    }
}
