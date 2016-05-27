package com.syncano.library;

import android.support.test.runner.AndroidJUnit4;

import com.syncano.library.Model.SomeV1;
import com.syncano.library.Model.SomeV2;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.callbacks.SyncanoListCallback;
import com.syncano.library.choice.Case;
import com.syncano.library.choice.SortOrder;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.offline.OfflineMode;
import com.syncano.library.offline.OfflineHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
    public void testGetObjectsWhereQueries() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV1.class);
        createClass(SomeV2.class);

        // create objects on server
        int itemsNum = 10;
        ArrayList<SomeV2> list = new ArrayList<>();
        Date date = new Date();
        for (int i = 0; i < itemsNum; i++) {
            SomeV2 obj = new SomeV2();
            obj.someInt = i;
            obj.someText = "num" + i;
            obj.someDate = new Date(date.getTime() + 1000);
            list.add(obj);
        }
        list.get(0).someText = "Lorem ipsum";
        list.get(1).someText = "dolor sit amet";
        list.get(2).someText = "Proin adipiscing";
        list.get(3).someText = "proin sIt quam aMet";
        list.get(4).someText = "proin lala quam om";
        list.get(5).someText = null;
        list.get(3).someDate = new Date(date.getTime() - 1000);
        list.get(4).someDate = new Date(date.getTime() - 1000);
        list.get(5).someDate = new Date(date.getTime() - 1000);
        list.get(6).someDate = new Date(date.getTime() - 1000);
        list.get(7).someDate = new Date(date.getTime() - 1000);
        for (SomeV2 obj : list) {
            assertTrue(obj.save().isSuccess());
        }

        // get all from server and write to db
        ResponseGetList<SomeV2> resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).
                cleanStorageOnSuccessDownload(true).getAll(true).get();
        assertTrue(resp.isSuccess());
        assertEquals(itemsNum, resp.getData().size());
        assertFalse(resp.isDataFromLocalStorage());

        // get all from db
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertTrue(resp.isSuccess());
        assertEquals(itemsNum, resp.getData().size());
        assertTrue(resp.isDataFromLocalStorage());

        // test where queries when getting from db
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).orderBy(SomeV2.FIELD_INT, SortOrder.DESCENDING)
                .where().gt(SomeV2.FIELD_INT, 4).lt(SomeV2.FIELD_DATE, date).get();
        assertTrue(resp.isSuccess());
        assertEquals(3, resp.getData().size());
        assertEquals(7, resp.getData().get(0).someInt);

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .gte(SomeV2.FIELD_INT, 4).lte(SomeV2.FIELD_INT, 6).isNotNull(SomeV2.FIELD_TEXT).get();
        assertTrue(resp.isSuccess());
        assertEquals(2, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where().
                isNull(SomeV2.FIELD_TEXT).neq(SomeV2.FIELD_INT, 2).get();
        assertTrue(resp.isSuccess());
        assertEquals(1, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .eq(SomeV2.FIELD_DATE, list.get(3).someDate).get();
        assertTrue(resp.isSuccess());
        assertEquals(5, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .eq(SomeV2.FIELD_TEXT, "lorem IPSUM", Case.INSENSITIVE).get();
        assertTrue(resp.isSuccess());
        assertEquals(1, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .eq(SomeV2.FIELD_TEXT, "lorem IPSUM").get();
        assertTrue(resp.isSuccess());
        assertEquals(0, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .in(SomeV2.FIELD_TEXT, new String[]{"Lorem ipsum", "dolor sit amet"}).get();
        assertTrue(resp.isSuccess());
        assertEquals(2, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .startsWith(SomeV2.FIELD_TEXT, "proin", Case.INSENSITIVE).get();
        assertTrue(resp.isSuccess());
        assertEquals(3, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .startsWith(SomeV2.FIELD_TEXT, "proin").get();
        assertTrue(resp.isSuccess());
        assertEquals(2, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .endsWith(SomeV2.FIELD_TEXT, "amet").get();
        assertTrue(resp.isSuccess());
        assertEquals(1, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .endsWith(SomeV2.FIELD_TEXT, "amet", Case.INSENSITIVE).get();
        assertTrue(resp.isSuccess());
        assertEquals(2, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .contains(SomeV2.FIELD_TEXT, "sit").get();
        assertTrue(resp.isSuccess());
        assertEquals(1, resp.getData().size());

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).where()
                .contains(SomeV2.FIELD_TEXT, "sit", Case.INSENSITIVE).get();
        assertTrue(resp.isSuccess());
        assertEquals(2, resp.getData().size());

        // test get one
        SomeV2 item = resp.getData().get(0);
        int id = item.getId();

        Response<SomeV2> respOne = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get(id);
        assertTrue(respOne.isSuccess());
        SomeV2 newItem = respOne.getData();
        assertEquals(item.someDate, newItem.someDate);
        assertEquals(item.someText, newItem.someText);
        assertEquals(item.someInt, newItem.someInt);
    }

    @Test
    public void testSimpleSaveFetchDelete() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);

        SomeV2 obj = new SomeV2();
        obj.someInt = 4;
        obj.someText = "lalala";
        obj.someDate = new Date();

        assertTrue(obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).save().isSuccess());

        SomeV2 newObj = new SomeV2();
        newObj.setId(obj.getId());
        newObj.mode(OfflineMode.LOCAL).fetch();

        assertEquals(obj.someDate, newObj.someDate);
        assertEquals(obj.someInt, newObj.someInt);
        assertEquals(obj.someText, newObj.someText);

        assertTrue(newObj.mode(OfflineMode.LOCAL).delete().isSuccess());

        ResponseGetList<SomeV2> resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertTrue(resp.isSuccess());
        assertEquals(0, resp.getData().size());
    }

    @Test
    public void testSaveModes() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);

        // local
        SomeV2 obj = SomeV2.generateObject();
        SomeV2 copy = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.LOCAL).save();

        ResponseGetList<SomeV2> resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());
        assertNull(resp.getData().get(0).getId());
        assertNotNull(resp.getData().get(0).getLocalId());
        assertTrue(copy.fieldsEqual(resp.getData().get(0)));

        // online, no save local
        obj = SomeV2.generateObject();
        copy = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(false).cleanStorageOnSuccessDownload(false).save();

        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).get();
        assertEquals(1, resp.getData().size());
        assertNull(resp.getData().get(0).getLocalId());
        assertNotNull(resp.getData().get(0).getId());
        assertTrue(copy.fieldsEqual(resp.getData().get(0)));
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());
        assertNull(resp.getData().get(0).getId());
        assertNotNull(resp.getData().get(0).getLocalId());

        // online, save local, not clear local
        obj = SomeV2.generateObject();
        copy = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).cleanStorageOnSuccessDownload(false).save();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(2, resp.getData().size()); // includes item from local test
        for (SomeV2 item : resp.getData()) {
            if (item.getId() != null) {
                assertTrue(copy.fieldsEqual(item));
            }
        }

        // online, save local, clear local
        obj = SomeV2.generateObject();
        copy = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).cleanStorageOnSuccessDownload(true).save();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());
        assertNotNull(resp.getData().get(0).getLocalId());
        assertNotNull(resp.getData().get(0).getId());
        assertTrue(copy.fieldsEqual(resp.getData().get(0)));

        // local bg online, save local, clear local
        obj = SomeV2.generateObject();
        final CountDownLatch latch1 = new CountDownLatch(1);
        final SomeV2 copy1 = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND).saveDownloadedDataToStorage(true).cleanStorageOnSuccessDownload(true)
                .backgroundCallback(new SyncanoCallback<SomeV2>() {
                    @Override
                    public void success(Response<SomeV2> response, SomeV2 result) {
                        assertNotNull(result.getLocalId());
                        assertNotNull(result.getId());
                        assertTrue(copy1.fieldsEqual(result));
                        latch1.countDown();
                    }

                    @Override
                    public void failure(Response<SomeV2> response) {
                        fail();
                    }
                }).save();
        latch1.await(10, TimeUnit.SECONDS);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());

        // local bg online, save local, not clear local
        obj = SomeV2.generateObject();
        final CountDownLatch latch2 = new CountDownLatch(1);
        final SomeV2 copy2 = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND).saveDownloadedDataToStorage(true).cleanStorageOnSuccessDownload(false)
                .backgroundCallback(new SyncanoCallback<SomeV2>() {
                    @Override
                    public void success(Response<SomeV2> response, SomeV2 result) {
                        assertNotNull(result.getLocalId());
                        assertNotNull(result.getId());
                        assertTrue(copy2.fieldsEqual(result));
                        latch2.countDown();
                    }

                    @Override
                    public void failure(Response<SomeV2> response) {
                        fail();
                    }
                }).save();
        latch2.await(10, TimeUnit.SECONDS);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(2, resp.getData().size());

        // local bg online, not save local, clear local
        obj = SomeV2.generateObject();
        final CountDownLatch latch3 = new CountDownLatch(1);
        final SomeV2 copy3 = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND).saveDownloadedDataToStorage(false).cleanStorageOnSuccessDownload(true)
                .backgroundCallback(new SyncanoCallback<SomeV2>() {
                    @Override
                    public void success(Response<SomeV2> response, SomeV2 result) {
                        assertNotNull(result.getLocalId());
                        assertNotNull(result.getId());
                        assertTrue(copy3.fieldsEqual(result));
                        latch3.countDown();
                    }

                    @Override
                    public void failure(Response<SomeV2> response) {
                        fail();
                    }
                }).save();
        latch3.await(10, TimeUnit.SECONDS);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());

        // local when online failed, but it will not fail
        obj = SomeV2.generateObject();
        copy = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).saveDownloadedDataToStorage(true).cleanStorageOnSuccessDownload(true).save();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());
        assertTrue(copy.fieldsEqual(obj));
        assertNotNull(obj.getId());

        // local when online really failed
        new SyncanoBuilder().instanceName("bad_instance").apiKey("bad_key").androidContext(getContext())
                .setAsGlobalInstance(true).build();
        obj = SomeV2.generateObject();
        copy = SomeV2.makeCopy(obj);
        obj.mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).saveDownloadedDataToStorage(true).save();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(2, resp.getData().size());
        assertTrue(copy.fieldsEqual(obj));
        assertNull(obj.getId());
        assertNotNull(obj.getLocalId());
    }

    @Test
    public void testFetchModes() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);
        final SomeV2 obj = SomeV2.generateObject();
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).save();

        // local
        SomeV2 newObj = new SomeV2();
        newObj.setId(obj.getId());
        newObj.mode(OfflineMode.LOCAL).fetch();
        assertTrue(obj.fieldsEqual(newObj));

        // online, no save local, clear storage
        newObj = new SomeV2();
        newObj.setId(obj.getId());
        newObj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(false).cleanStorageOnSuccessDownload(true).fetch();
        assertTrue(obj.fieldsEqual(newObj));
        ResponseGetList<SomeV2> resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());

        // online, save local
        newObj = new SomeV2();
        newObj.setId(obj.getId());
        newObj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).fetch();
        assertTrue(obj.fieldsEqual(newObj));
        assertNotNull(obj.getLocalId());
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());

        // local bg online, save local
        newObj = new SomeV2();
        newObj.setId(obj.getId());
        final CountDownLatch latch = new CountDownLatch(1);
        newObj.mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND).saveDownloadedDataToStorage(true)
                .backgroundCallback(new SyncanoCallback<SomeV2>() {
                    @Override
                    public void success(Response<SomeV2> response, SomeV2 result) {
                        assertNotNull(result.getLocalId());
                        assertNotNull(result.getId());
                        assertTrue(obj.fieldsEqual(result));
                        latch.countDown();
                    }

                    @Override
                    public void failure(Response<SomeV2> response) {
                        fail();
                    }
                }).fetch();
        assertNotNull(newObj.getLocalId());
        assertNotNull(newObj.getId());
        assertTrue(obj.fieldsEqual(newObj));
        latch.await(10, TimeUnit.SECONDS);

        // local when online failed, but it will not fail
        newObj = new SomeV2();
        newObj.setId(obj.getId());
        Response<SomeV2> respItem = newObj.mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).saveDownloadedDataToStorage(true).fetch();
        assertNotNull(newObj.getLocalId());
        assertNotNull(newObj.getId());
        assertTrue(obj.fieldsEqual(newObj));
        assertFalse(respItem.isDataFromLocalStorage());

        // local when online really failed
        new SyncanoBuilder().instanceName("bad_instance").apiKey("bad_key").androidContext(getContext())
                .setAsGlobalInstance(true).build();
        newObj = new SomeV2();
        newObj.setId(obj.getId());
        respItem = newObj.mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).fetch();
        assertNotNull(newObj.getLocalId());
        assertNotNull(newObj.getId());
        assertTrue(obj.fieldsEqual(newObj));
        assertTrue(respItem.isDataFromLocalStorage());
    }

    @Test
    public void testDeleteModes() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);
        SomeV2 obj = SomeV2.generateObject();
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).save();
        ResponseGetList<SomeV2> resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).get();
        assertEquals(1, resp.getData().size());

        // local
        obj.mode(OfflineMode.LOCAL).delete();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).get();
        assertEquals(1, resp.getData().size());

        // online, no save
        obj.mode(OfflineMode.ONLINE).delete();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(false).get();
        assertEquals(0, resp.getData().size());
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());

        // online, save
        OfflineHelper.clearTable(syncano.getAndroidContext(), SomeV2.class);
        obj = SomeV2.generateObject();
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).save();
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).delete();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).get();
        assertEquals(0, resp.getData().size());

        // local bg online
        OfflineHelper.clearTable(syncano.getAndroidContext(), SomeV2.class);
        obj = SomeV2.generateObject();
        obj.mode(OfflineMode.ONLINE).saveDownloadedDataToStorage(true).save();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(1, resp.getData().size());
        final CountDownLatch latch = new CountDownLatch(1);
        obj.mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND).saveDownloadedDataToStorage(true)
                .backgroundCallback(new SyncanoCallback<SyncanoObject>() {
                    @Override
                    public void success(Response<SyncanoObject> response, SyncanoObject result) {
                        latch.countDown();
                    }

                    @Override
                    public void failure(Response<SyncanoObject> response) {
                        fail();
                    }
                }).delete();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());
        latch.await(10, TimeUnit.SECONDS);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE).get();
        assertEquals(0, resp.getData().size());
    }

    @Test
    public void testGetListModes() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);
        final int number = 5;
        for (int i = 0; i < number; i++) {
            assertTrue(SomeV2.generateObject().save().isSuccess());
        }

        // local empty
        ResponseGetList<SomeV2> resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());

        // online save
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE)
                .saveDownloadedDataToStorage(true).get();
        assertEquals(number, resp.getData().size());

        // local not empty
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(number, resp.getData().size());

        // online not save clear
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.ONLINE)
                .saveDownloadedDataToStorage(false).cleanStorageOnSuccessDownload(true).get();
        assertEquals(number, resp.getData().size());

        // local empty again
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(0, resp.getData().size());

        // background online
        final CountDownLatch latch = new CountDownLatch(1);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND)
                .saveDownloadedDataToStorage(true).cleanStorageOnSuccessDownload(true)
                .backgroundCallback(new SyncanoListCallback<SomeV2>() {
                    @Override
                    public void success(ResponseGetList<SomeV2> response, List<SomeV2> result) {
                        assertEquals(number, result.size());
                    }

                    @Override
                    public void failure(ResponseGetList<SomeV2> response) {
                        fail();
                    }
                }).get();
        assertEquals(0, resp.getData().size());
        latch.await(10, TimeUnit.SECONDS);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        assertEquals(number, resp.getData().size());

        // local when online failed, but it will not fail
        OfflineHelper.clearTable(syncano.getAndroidContext(), SomeV2.class);
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED)
                .saveDownloadedDataToStorage(true).get();
        assertEquals(number, resp.getData().size());
        assertFalse(resp.isDataFromLocalStorage());

        // local when online really failed
        new SyncanoBuilder().instanceName("bad_instance").apiKey("bad_key").androidContext(getContext())
                .setAsGlobalInstance(true).build();
        resp = Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).get();
        assertEquals(number, resp.getData().size());
        assertTrue(resp.isDataFromLocalStorage());
    }

    @Test
    public void checkNoContext() throws InterruptedException {
        new SyncanoBuilder().customServerUrl(BuildConfig.STAGING_SERVER_URL).instanceName(BuildConfig.INSTANCE_NAME)
                .apiKey(BuildConfig.API_KEY).setAsGlobalInstance(true).build();
        boolean exception = false;
        try {
            createClass(SomeV2.class);
            Syncano.please(SomeV2.class).mode(OfflineMode.LOCAL).get();
        } catch (RuntimeException re) {
            assertTrue(re.getMessage().toLowerCase().contains("context"));
            exception = true;
        }
        assertTrue(exception);
    }

    @Test
    public void checkIncrement() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);

        // online
        SomeV2 obj = SomeV2.generateObject();
        assertTrue(obj.save().isSuccess());
        int initVal = obj.someInt;
        obj.increment(SomeV2.FIELD_INT, 1).save();
        assertTrue(initVal + 1 == obj.someInt);

        // it doesn't work offline, but it tells about it in logs
        initVal = obj.someInt;
        obj.increment(SomeV2.FIELD_INT, 1).mode(OfflineMode.LOCAL).save();
        assertTrue(initVal == obj.someInt); // not incremented
    }

    @Test
    public void clearingFields() throws InterruptedException {
        OfflineHelper.deleteDatabase(syncano.getAndroidContext(), SomeV2.class);
        createClass(SomeV2.class);

        // online
        SomeV2 obj = SomeV2.generateObject();
        assertTrue(obj.save().isSuccess());
        obj.clearField(SomeV2.FIELD_DATE).save();
        assertNull(obj.someDate);
        assertFalse(obj.hasAnyFieldsToClear());

        // offline
        obj = SomeV2.generateObject();
        assertTrue(obj.saveDownloadedDataToStorage(true).save().isSuccess());
        obj.clearField(SomeV2.FIELD_DATE).mode(OfflineMode.LOCAL).save();
        assertNull(obj.someDate);
        assertFalse(obj.hasAnyFieldsToClear());

        // offline, background online
        obj = SomeV2.generateObject();
        assertTrue(obj.saveDownloadedDataToStorage(true).save().isSuccess());
        final CountDownLatch latch = new CountDownLatch(1);
        obj.clearField(SomeV2.FIELD_DATE).mode(OfflineMode.LOCAL_ONLINE_IN_BACKGROUND)
                .backgroundCallback(new SyncanoCallback<SomeV2>() {
                    @Override
                    public void success(Response<SomeV2> response, SomeV2 result) {
                        assertNull(result.someDate);
                        assertFalse(result.hasAnyFieldsToClear());
                        latch.countDown();
                    }

                    @Override
                    public void failure(Response<SomeV2> response) {
                        fail();
                    }
                }).save();
        assertNull(obj.someDate);
        latch.await(10, TimeUnit.SECONDS);
        assertEquals(0, latch.getCount());

        // local when online failed, but it will not fail
        obj = SomeV2.generateObject();
        assertTrue(obj.saveDownloadedDataToStorage(true).save().isSuccess());
        Response<SomeV2> resp = obj.clearField(SomeV2.FIELD_DATE).mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).save();
        assertNull(obj.someDate);
        assertFalse(obj.hasAnyFieldsToClear());
        assertFalse(resp.isDataFromLocalStorage());

        // local when online really failed
        obj = SomeV2.generateObject();
        assertTrue(obj.saveDownloadedDataToStorage(true).save().isSuccess());
        new SyncanoBuilder().instanceName("bad_instance").apiKey("bad_key").androidContext(getContext())
                .setAsGlobalInstance(true).build();
        resp = obj.clearField(SomeV2.FIELD_DATE).mode(OfflineMode.LOCAL_WHEN_ONLINE_FAILED).save();
        assertNull(obj.someDate);
        assertFalse(obj.hasAnyFieldsToClear());
        assertTrue(resp.isDataFromLocalStorage());
    }
}
