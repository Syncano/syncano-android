package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineHelper;
import com.syncano.library.offline.OfflineMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

    @SyncanoClass(name = CLASS_NAME, getMode = OfflineMode.LOCAL_ONLINE_IN_BACKGROUND, saveMode = OfflineMode.ONLINE,
            saveDownloadedDataToStorage = true, cleanStorageOnSuccessDownload = true)
    private static class SaveClearLocalBgOnline extends SyncanoObject {
        @SyncanoField(name = FIELD_NAME)
        String val = "some value";
    }

    @Test
    public void testBgOnlineSaveClear() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SaveClearLocalBgOnline.class);
        createClass(SaveClearLocalBgOnline.class);

        // check if local and online return empty list
        final CountDownLatch latch = new CountDownLatch(1);
        ResponseGetList<SaveClearLocalBgOnline> resp = Syncano.please(SaveClearLocalBgOnline.class)
                .backgroundCallback(new SyncanoListCallback<SaveClearLocalBgOnline>() {
                    @Override
                    public void success(ResponseGetList<SaveClearLocalBgOnline> response, List<SaveClearLocalBgOnline> result) {
                        assertTrue(response.isSuccess());
                        assertFalse(response.isDataFromLocalStorage());
                        assertEquals(0, response.getData().size());
                        latch.countDown();
                    }

                    @Override
                    public void failure(ResponseGetList<SaveClearLocalBgOnline> response) {
                        fail();
                    }
                }).get();
        assertTrue(resp.isSuccess());
        assertTrue(resp.isDataFromLocalStorage());
        assertEquals(0, resp.getData().size());
        latch.await(10, TimeUnit.SECONDS);

        // create item, bg task shouldn't be active
        final CountDownLatch latch2 = new CountDownLatch(1);
        Response<SaveClearLocalBgOnline> respSave = new SaveClearLocalBgOnline()
                .backgroundCallback(new SyncanoCallback<SaveClearLocalBgOnline>() {
                    @Override
                    public void success(Response<SaveClearLocalBgOnline> response, SaveClearLocalBgOnline result) {
                        latch2.countDown();
                    }

                    @Override
                    public void failure(Response<SaveClearLocalBgOnline> response) {
                        fail();
                    }
                }).save();
        assertTrue(respSave.isSuccess());
        latch2.await(5, TimeUnit.SECONDS);
        assertEquals(1, latch2.getCount());

        // create second item
        assertTrue(new SaveClearLocalBgOnline().save().isSuccess());

        // both items should be local and online
        final CountDownLatch latch3 = new CountDownLatch(1);
        resp = Syncano.please(SaveClearLocalBgOnline.class)
                .backgroundCallback(new SyncanoListCallback<SaveClearLocalBgOnline>() {
                    @Override
                    public void success(ResponseGetList<SaveClearLocalBgOnline> response, List<SaveClearLocalBgOnline> result) {
                        assertTrue(response.isSuccess());
                        assertFalse(response.isDataFromLocalStorage());
                        assertEquals(2, response.getData().size());
                        latch3.countDown();
                    }

                    @Override
                    public void failure(ResponseGetList<SaveClearLocalBgOnline> response) {
                        fail();
                    }
                }).get();
        assertTrue(resp.isSuccess());
        assertTrue(resp.isDataFromLocalStorage());
        assertEquals(2, resp.getData().size());
        latch3.await(10, TimeUnit.SECONDS);

        // fetching one item
        final SaveClearLocalBgOnline oldItem = resp.getData().get(0);
        final SaveClearLocalBgOnline newItem = new SaveClearLocalBgOnline();
        newItem.setId(oldItem.getId());
        final CountDownLatch latch4 = new CountDownLatch(1);
        final Response<SaveClearLocalBgOnline> respItem = newItem.backgroundCallback(new SyncanoCallback<SaveClearLocalBgOnline>() {
            @Override
            public void success(Response<SaveClearLocalBgOnline> response, SaveClearLocalBgOnline result) {
                assertEquals(oldItem.val, result.val);
                assertFalse(response.isDataFromLocalStorage());
                latch4.countDown();
            }

            @Override
            public void failure(Response<SaveClearLocalBgOnline> response) {
                fail();
            }
        }).fetch();
        assertEquals(oldItem.val, newItem.val);
        assertTrue(respItem.isDataFromLocalStorage());
        latch4.await(10, TimeUnit.SECONDS);
        assertEquals(0, latch4.getCount());
        assertEquals(2, Syncano.please(SaveClearLocalBgOnline.class).mode(OfflineMode.LOCAL).get().getData().size());

        // deleting one item
        final CountDownLatch latch5 = new CountDownLatch(1);
        Response<SaveClearLocalBgOnline> respDelete = oldItem
                .backgroundCallback(new SyncanoCallback<SaveClearLocalBgOnline>() {
                    @Override
                    public void success(Response<SaveClearLocalBgOnline> response, SaveClearLocalBgOnline result) {
                        latch5.countDown();
                    }

                    @Override
                    public void failure(Response<SaveClearLocalBgOnline> response) {
                        fail();
                    }
                }).delete();
        assertTrue(respDelete.isSuccess());
        latch5.await(5, TimeUnit.SECONDS);
        assertEquals(1, latch5.getCount());
        assertEquals(1, Syncano.please(SaveClearLocalBgOnline.class).mode(OfflineMode.LOCAL).get().getData().size());
        assertEquals(1, Syncano.please(SaveClearLocalBgOnline.class).mode(OfflineMode.ONLINE).get().getData().size());
    }
}
