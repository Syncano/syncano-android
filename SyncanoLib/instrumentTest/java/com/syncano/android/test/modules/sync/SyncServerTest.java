
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
import com.syncano.android.lib.modules.data.ParamsDataDelete;
import com.syncano.android.lib.modules.data.ParamsDataNew;
import com.syncano.android.lib.modules.data.ParamsDataUpdate;
import com.syncano.android.lib.modules.data.ResponseDataNew;
import com.syncano.android.lib.modules.notification.ParamsNotificationSend;
import com.syncano.android.lib.modules.projects.ParamsProjectDelete;
import com.syncano.android.lib.modules.projects.ParamsProjectNew;
import com.syncano.android.lib.modules.projects.ResponseProjectNew;
import com.syncano.android.lib.modules.subscriptions.ParamsSubscriptionSubscribeCollection;
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

    private static final String DATA_TEXT = "Data test text.";
    private static final String UPDATED_TEXT = "Update data test text";
    private static final String PARAM_NOTIFICATION_TEXT = "text";
    private static final String NOTIFICATION_TEXT = "Notification text";

    private final static int WAITING_MILLIS = 120000;

    private SyncServerConnection conn;
    private CountDownLatch lock;
    private CountDownLatch subscriptionLock;
    private CountDownLatch notificationLock;

    private Syncano syncano = null;
    private Response response = null;
    private boolean success = false;
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

    public void testSubscription() throws Exception {
        // Subscribe
        ParamsSubscriptionSubscribeCollection paramsSubscriptionSubscribeCollection = new ParamsSubscriptionSubscribeCollection(projectId, collectionId);
        lock = new CountDownLatch(1);
        response = null;
        conn.call(paramsSubscriptionSubscribeCollection, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                SyncServerTest.this.response = response;
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertNotNull(response);
        assertEquals(Response.CODE_SUCCESS, (int) response.getResultCode());

        // Data New
        subscriptionLock = new CountDownLatch(1);
        success = false;
        conn.setSubscriptionListener(new SubscriptionListenerImpl() {
            @Override
            public void added(Data data) {
                assertNotNull(data);
                assertEquals(DATA_TEXT, data.getText());
                success = true;
                subscriptionLock.countDown();
            }
        });

        ParamsDataNew paramsDataNew = new ParamsDataNew(projectId, collectionId, null, Data.PENDING);
        paramsDataNew.setText(DATA_TEXT);
        lock = new CountDownLatch(1);
        response = null;
        conn.call(paramsDataNew, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                SyncServerTest.this.response = response;
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertNotNull(response);
        assertEquals(Response.CODE_SUCCESS, (int) response.getResultCode());
        String dataId = ((ResponseDataNew) response).getData().getId();

        // Check for subscription success
        subscriptionLock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(success);

        // Data Update
        subscriptionLock = new CountDownLatch(1);
        success = false;
        conn.setSubscriptionListener(new SubscriptionListenerImpl() {
            @Override
            public void changed(ArrayList<DataChanges> changes) {
                assertNotNull(changes);
                success = true;
                subscriptionLock.countDown();
            }
        });

        ParamsDataUpdate paramsDataUpdate = new ParamsDataUpdate(projectId, collectionId, null, dataId, null);
        paramsDataUpdate.setText(UPDATED_TEXT);
        lock = new CountDownLatch(1);
        response = null;
        conn.call(paramsDataUpdate, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                SyncServerTest.this.response = response;
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertNotNull(response);
        assertEquals(Response.CODE_SUCCESS, (int) response.getResultCode());

        // Check for subscription success
        subscriptionLock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(success);

        // Data Delete
        subscriptionLock = new CountDownLatch(1);
        success = false;
        conn.setSubscriptionListener(new SubscriptionListenerImpl() {
            @Override
            public void deleted(String[] ids) {
                assertNotNull(ids);
                assertTrue(ids.length > 0);
                success = true;;
                subscriptionLock.countDown();
            }
        });

        ParamsDataDelete paramsDataDelete = new ParamsDataDelete(projectId, collectionId, null);
        paramsDataDelete.setDataIds(new String[] {dataId});
        lock = new CountDownLatch(1);
        response = null;
        conn.call(paramsDataDelete, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                SyncServerTest.this.response = response;
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertNotNull(response);
        assertEquals(Response.CODE_SUCCESS, (int) response.getResultCode());

        // Check for subscription success
        subscriptionLock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(success);
    }

    public void testNotifications() throws Exception {
        notificationLock = new CountDownLatch(1);
        success = false;
        conn.setSyncServerListener(new SyncServerListenerImpl() {
            @Override
            public void message(String object, JsonObject message) {
                assertNotNull(object);
                assertNotNull(message);
                assertEquals(NOTIFICATION_TEXT, message.get(PARAM_NOTIFICATION_TEXT).getAsString());
                success = true;
                notificationLock.countDown();
            }
        });

        ParamsNotificationSend paramsNotificationSend = new ParamsNotificationSend(null);
        paramsNotificationSend.addParam(PARAM_NOTIFICATION_TEXT, NOTIFICATION_TEXT);

        lock = new CountDownLatch(1);
        response = null;
        conn.call(paramsNotificationSend, new SyncServerCallback() {
            @Override
            public void result(Response response) {
                SyncServerTest.this.response = response;
                lock.countDown();
            }
        });
        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertNotNull(response);
        assertEquals(Response.CODE_SUCCESS, (int) response.getResultCode());

        // Check for notification success
        notificationLock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        assertTrue(success);
    }

    private class SubscriptionListenerImpl implements SubscriptionListener {

        @Override
        public void deleted(String[] ids) {}

        @Override
        public void changed(ArrayList<DataChanges> changes) {}

        @Override
        public void added(Data data) {}
    }

    private class SyncServerListenerImpl implements SyncServerListener {

        @Override
        public void disconnected() {}

        @Override
        public void error(String why) {}

        @Override
        public void connected() {}

        @Override
        public void message(String object, JsonObject message) {}
    }
}
