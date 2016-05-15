package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineHelper;
import com.syncano.library.offline.OfflineMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class OfflineClassDefaultsTest extends SyncanoAndroidTestCase {
    private final static String CLASS_NAME = "test_class";
    private final static String FIELD_NAME = "test_field";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        OfflineHelper.reinitHelper();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @SyncanoClass(name = CLASS_NAME)
    private static class NotSaveLocal extends SyncanoObject {
        @SyncanoField(name = FIELD_NAME)
        String val = "some value";
    }

    @Test
    public void notSaveLocal() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), NotSaveLocal.class);
        createClass(NotSaveLocal.class);
        assertTrue(new NotSaveLocal().save().isSuccess());

        assertEquals(0, Syncano.please(NotSaveLocal.class).mode(OfflineMode.LOCAL).get().getData().size());
    }

    @SyncanoClass(name = CLASS_NAME, saveDownloadedDataToStorage = true)
    private static class SaveLocal extends SyncanoObject {
        @SyncanoField(name = FIELD_NAME)
        String val = "some value";
    }

    @Test
    public void saveLocal() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SaveLocal.class);
        createClass(SaveLocal.class);
        assertTrue(new SaveLocal().save().isSuccess());

        assertEquals(1, Syncano.please(SaveLocal.class).mode(OfflineMode.LOCAL).get().getData().size());
    }

    @Test
    public void notClearLocal() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SaveLocal.class);
        createClass(SaveLocal.class);

        assertTrue(new SaveLocal().save().isSuccess());
        assertEquals(1, Syncano.please(SaveLocal.class).mode(OfflineMode.LOCAL).get().getData().size());
        assertTrue(new SaveLocal().save().isSuccess());
        assertEquals(2, Syncano.please(SaveLocal.class).mode(OfflineMode.LOCAL).get().getData().size());
    }

    @SyncanoClass(name = CLASS_NAME, saveDownloadedDataToStorage = true, cleanStorageOnSuccessDownload = true)
    private static class SaveClearLocal extends SyncanoObject {
        @SyncanoField(name = FIELD_NAME)
        String val = "some value";
    }

    @Test
    public void clearLocal() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SaveClearLocal.class);
        createClass(SaveClearLocal.class);

        assertTrue(new SaveClearLocal().mode(OfflineMode.LOCAL).save().isSuccess());
        assertTrue(new SaveClearLocal().mode(OfflineMode.ONLINE).save().isSuccess());
        assertTrue(new SaveClearLocal().mode(OfflineMode.ONLINE).save().isSuccess());
        assertEquals(3, Syncano.please(SaveClearLocal.class).mode(OfflineMode.LOCAL).get().getData().size());
        assertEquals(2, Syncano.please(SaveClearLocal.class).mode(OfflineMode.ONLINE).get().getData().size());
        assertEquals(2, Syncano.please(SaveClearLocal.class).mode(OfflineMode.LOCAL).get().getData().size());
    }
}
