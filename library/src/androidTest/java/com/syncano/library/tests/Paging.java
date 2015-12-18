package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoObject;

import java.util.ArrayList;
import java.util.List;

public class Paging extends SyncanoApplicationTestCase {

    public static final int NUMBER = 22;
    public static final int PAGE = 10;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createClass(TestSyncanoObject.class);

        for (int i = 0; i < NUMBER; i++) {
            assertTrue((new TestSyncanoObject()).save().isSuccess());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        removeClass(TestSyncanoObject.class);
        super.tearDown();
    }

    public void testGetAllObjects() {
        ArrayList<TestSyncanoObject> list = new ArrayList<>();

        boolean allDownloaded = false;
        int lastPk = 0;
        while(!allDownloaded) {
            Response<List<TestSyncanoObject>> resp = syncano.getObjects(TestSyncanoObject.class).
            assertTrue(resp.isSuccess());
            list.addAll(resp.getData());
        }


    }
}
