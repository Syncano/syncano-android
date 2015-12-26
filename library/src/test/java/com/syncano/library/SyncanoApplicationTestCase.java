package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.choice.ClassStatus;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.SyncanoLogger;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SyncanoApplicationTestCase {

    protected Syncano syncano;

    public SyncanoApplicationTestCase() {
    }

    public void setUp() throws Exception {
        Syncano.init(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY, BuildConfig.INSTANCE_NAME);
        syncano = Syncano.getInstance();
        SyncanoLog.initLogger(new SyncanoLogger() {
            @Override
            public void d(String tag, String message) {
                log(tag, message);
            }

            @Override
            public void w(String tag, String message) {
                log(tag, message);
            }

            @Override
            public void e(String tag, String message) {
                log(tag, message);
            }
        });
    }

    public void log(String tag, String message) {
        System.out.println(tag + ", " + message);
    }

    public void createClass(Class<? extends SyncanoObject> clazz) throws InterruptedException {
        removeClass(clazz);
        Response resp = syncano.createSyncanoClass(clazz).send();
        assertTrue(resp.isSuccess());

        long start = System.currentTimeMillis();
        SyncanoClass downloadedClass = null;
        while (System.currentTimeMillis() - start < 180000 && (downloadedClass == null || downloadedClass.getStatus() != ClassStatus.READY)) {
            SyncanoLog.d(SyncanoApplicationTestCase.class.getSimpleName(), "Waiting for class to create: " + (System.currentTimeMillis() - start));
            Response<SyncanoClass> respClass = syncano.getSyncanoClass(clazz).send();
            assertTrue(respClass.isSuccess());
            downloadedClass = respClass.getData();
            if (downloadedClass != null && downloadedClass.getStatus() == ClassStatus.READY) {
                break;
            }
            Thread.sleep(100);
        }
        assertNotNull(downloadedClass);
        assertEquals(SyncanoClassHelper.getSyncanoClassSchema(clazz), downloadedClass.getSchema());
        assertEquals(ClassStatus.READY, downloadedClass.getStatus());
    }

    public void removeClass(Class<? extends SyncanoObject> clazz) {
        Response<SyncanoClass> resp = syncano.deleteSyncanoClass(clazz).send();
        assertTrue(resp.isSuccess());
    }

    public void tearDown() throws Exception {
        SyncanoLog.release();
    }

    public String generateString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz       ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}