
package com.syncano.android.test.modules.sync;

import android.test.AndroidTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonObject;
import com.syncano.android.lib.Syncano;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.modules.collections.ParamsCollectionDelete;
import com.syncano.android.lib.modules.collections.ParamsCollectionNew;
import com.syncano.android.lib.modules.collections.ResponseCollectionNew;
import com.syncano.android.lib.modules.data.ParamsDataGet;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.objects.Data;
import com.syncano.android.lib.syncserver.DataChanges;
import com.syncano.android.lib.syncserver.SyncServerConnection;
import com.syncano.android.lib.syncserver.SyncServerConnection.SyncServerCallback;
import com.syncano.android.lib.syncserver.SyncServerConnection.SyncServerListener;
import com.syncano.android.lib.syncserver.SyncServerConnection.SubscriptionListener;
import com.syncano.android.lib.utils.DownloadTool;
import com.syncano.android.test.config.Constants;

public class SyncServerTest extends AndroidTestCase {

    public final static String TAG = "SyncServerTest";
    private SyncServerConnection conn;
    private CountDownLatch lock;
    private final static int WAITING_MILLIS = 120000;
    private boolean wasResponse;

    private Syncano syncano = null;
    private String projectId = null;
    private String collectionId = null;

    @Override
    protected void setUp() throws Exception {
        syncano = new Syncano(mContext, Constants.INSTANCE_NAME, Constants.API_KEY);
        assertTrue("Device should be connected to internet before test", DownloadTool.connectionAvailable(mContext));

        // Create test project
        ParamsProjectNew paramsProjectNew = new ParamsProjectNew("CI Test Project");
        ResponseProjectNew responseProjectNew = syncano.projectNew(paramsProjectNew);
        assertEquals("Failed to create test project", Response.CODE_SUCCESS, (int) responseProjectNew.getResultCode());
        projectId = responseProjectNew.getProject().getId();

        // Create test collection
        ParamsCollectionNew paramsCollectionNew = new ParamsCollectionNew(projectId, "CI Test Collection");
        ResponseCollectionNew responseCollectionNew = syncano.collectionNew(paramsCollectionNew);
        assertEquals("Failed to create test collection", Response.CODE_SUCCESS, (int) responseCollectionNew.getResultCode());
        collectionId = responseCollectionNew.getCollection().getId();


        lock = new CountDownLatch(1);
        conn = new SyncServerConnection(mContext, Constants.INSTANCE_NAME, Constants.API_KEY,
                new SyncServerListener() {
                    @Override
                    public void message(String object, JsonObject message) {
                        Log.d(TAG, "message " + object + " " + message);
                    }

                    @Override
                    public void error(String why) {
                        Log.d(TAG, "error: " + why);
                    }

                    @Override
                    public void disconnected() {
                        Log.d(TAG, "disconnected");
                        lock.countDown();
                    }

                    @Override
                    public void connected() {
                        Log.d(TAG, "connected");
                        lock.countDown();
                    }
                }, new SubscriptionListener() {
                    @Override
                    public void deleted(int[] ids) {
                        Log.d(TAG, "deleted " + ids);
                    }

                    @Override
                    public void changed(ArrayList<DataChanges> changes) {
                        Log.d(TAG, "changed");
                    }

                    @Override
                    public void added(Data data) {
                        Log.d(TAG, "added");
                    }

                });

        conn.start();
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(conn.isConnected());
    }

    @Override
    protected void tearDown() throws Exception {

        conn.stop();

        // Clean test collection (called before project is deleted)
        ParamsCollectionDelete paramsCollectionDelete = new ParamsCollectionDelete(projectId, collectionId);
        Response responseCollectionDelete = syncano.collectionDelete(paramsCollectionDelete);
        assertEquals("Failed to clean test collection", Response.CODE_SUCCESS, (int)responseCollectionDelete.getResultCode());

        // Clean test project
        ParamsProjectDelete paramsProjectDelete = new ParamsProjectDelete(projectId);
        Response responseProjectDelete = syncano.projectDelete(paramsProjectDelete);
        assertEquals("Failed to clean test project", Response.CODE_SUCCESS, (int) responseProjectDelete.getResultCode());
    }

    public void testDataGet() throws Exception {
        ParamsDataGet params = new ParamsDataGet(projectId, collectionId, null);

        lock = new CountDownLatch(1);
        wasResponse = false;
        conn.call(params, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                wasResponse = true;
                assertTrue(response.getResultCode() == Response.CODE_SUCCESS);
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(wasResponse);
    }
}
