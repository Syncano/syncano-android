package com.syncano.library;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.syncano.library.api.Response;
import com.syncano.library.choice.ClassStatus;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.data.User;
import com.syncano.library.utils.SyncanoLogger;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import java.util.List;

public class SyncanoAndroidTestCase extends ApplicationTestCase<Application> {

    protected SyncanoDashboard syncano;

    public SyncanoAndroidTestCase() {
        super(Application.class);
    }

    public void setUp() throws Exception {
        syncano = new SyncanoDashboard(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY, BuildConfig.INSTANCE_NAME, getContext());
        Syncano.setInstance(syncano);
        SyncanoLog.initLogger(new SyncanoLogger() {
            @Override
            public void d(String tag, String message) {
                Log.d(tag, message);
            }

            @Override
            public void w(String tag, String message) {
                Log.w(tag, message);
            }

            @Override
            public void e(String tag, String message) {
                Log.e(tag, message);
            }
        });
    }


    public void createClass(Class<? extends SyncanoObject> clazz) throws InterruptedException {
        removeClass(clazz);
        Response resp = syncano.createSyncanoClass(clazz).send();
        assertTrue(resp.isSuccess());

        long start = System.currentTimeMillis();
        SyncanoClass downloadedClass = null;
        while (System.currentTimeMillis() - start < 180000 && (downloadedClass == null || downloadedClass.getStatus() != ClassStatus.READY)) {
            Thread.sleep(100);
            SyncanoLog.d(SyncanoAndroidTestCase.class.getSimpleName(), "Waiting for class to create: " + (System.currentTimeMillis() - start));
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

    public static void deleteTestUser(SyncanoDashboard syncano, String userName) {
        Response<List<User>> response = syncano.getUsers().send();

        if (response.getData() != null && response.getData().size() > 0) {
            for (User u : response.getData()) {
                if (userName.equals(u.getUserName())) {
                    syncano.deleteUser(u.getId()).send();
                    break;
                }
            }
        }
    }
}