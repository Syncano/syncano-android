package com.syncano.library;


import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OfflineOnlyLocalFields extends SyncanoAndroidTestCase {
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
    public void testOnlyLocalFields() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), WithLocalFields.class);
        createClass(WithLocalFields.class);

        Response<SyncanoClass> resp = syncano.getSyncanoClass("with_local").send();
        assertTrue(resp.isSuccess());
        SyncanoClass clazz = resp.getData();
        assertTrue(!clazz.getSchema().toString().contains("local"));

        WithLocalFields item = new WithLocalFields();
        item.onlineField = "aaa";
        item.localField = "bbb";
        assertTrue(item.save().isSuccess());

        assertEquals("bbb", item.localField);
    }

    @com.syncano.library.annotation.SyncanoClass(name = "with_local", saveDownloadedDataToStorage = true)
    static class WithLocalFields extends SyncanoObject {
        @SyncanoField(name = "online")
        String onlineField;
        @SyncanoField(name = "local", onlyLocal = true)
        String localField;
    }
}
