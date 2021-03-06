package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.choice.ClassStatus;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;
import com.syncano.library.utils.SyncanoLogger;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SyncanoApplicationTestCase {

    protected SyncanoDashboard syncano;

    public SyncanoApplicationTestCase() {
    }

    public void deleteTestUser(String userName) {
        Response<List<User>> response = syncano.getUsers().send();

        if (response.getData() != null && response.getData().size() > 0) {
            for (User u : response.getData()) {
                if (userName.equals(u.getUserName())) {
                    syncano.deleteUser(u.getId()).send();
                }
            }
        }
    }

    public void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Syncano.init(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY, BuildConfig.INSTANCE_NAME);
        syncano = new SyncanoDashboard(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY, BuildConfig.INSTANCE_NAME);
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
        while (System.currentTimeMillis() - start < (60 * 60 * 1000) && (downloadedClass == null || downloadedClass.getStatus() != ClassStatus.READY)) {
            Thread.sleep(100);
            SyncanoLog.d(SyncanoApplicationTestCase.class.getSimpleName(), "Waiting for class to create: " + (System.currentTimeMillis() - start));
            Response<SyncanoClass> respClass = syncano.getSyncanoClass(clazz).send();
            assertEquals(Response.HTTP_CODE_SUCCESS, respClass.getHttpResultCode());
            downloadedClass = respClass.getData();
            if (downloadedClass != null && downloadedClass.getStatus() == ClassStatus.READY) {
                break;
            }
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

    public String getAssetsDir() {
        return "./src/test/assets/";
    }

    public String getBuildsDir() {
        return "./build/";
    }
}