package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.Model.SomeV1;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.offline.GetMode;
import com.syncano.library.offline.OfflineHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OfflineTest extends SyncanoAndroidTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        OfflineHelper.reinitHelper();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testGetObjects() throws InterruptedException {
        createClass(SomeV1.class);

        int itemsNum = 5;
        for (int i = 0; i < itemsNum; i++) {
            SomeV1 obj = SomeV1.generateObject();
            obj.someInt = i;
            assertTrue(obj.save().isSuccess());
        }

        ResponseGetList<SomeV1> resp = Syncano.please(SomeV1.class).mode(GetMode.ONLINE).saveDownloadedDataToStorage(true).
                cleanStorageOnSuccessDownload(true).getAll(true).get();
        assertTrue(resp.isSuccess());
        assertEquals(itemsNum, resp.getData().size());
        assertFalse(resp.isDataFromLocalStorage());

        resp = Syncano.please(SomeV1.class).mode(GetMode.LOCAL).getAll(true).get();
        assertTrue(resp.isSuccess());
        assertEquals(itemsNum, resp.getData().size());
        assertTrue(resp.isDataFromLocalStorage());
    }
}
