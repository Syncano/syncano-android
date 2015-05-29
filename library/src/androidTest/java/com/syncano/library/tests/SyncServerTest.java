package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.google.gson.JsonObject;
import com.syncano.library.Config;
import com.syncano.library.SyncServer;
import com.syncano.library.Syncano;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoClass;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SyncServerTest extends ApplicationTestCase<Application> {

    private static final String TAG = SyncServerTest.class.getSimpleName();
    private final static int WAITING_MILLIS = 120000;

    private Syncano syncano;
    private SyncServer syncServer;
    private Channel channel;
    private CountDownLatch lock;

    private static final String CHANNEL_NAME = "channel_one";
    private static final String ROOM_NAME = "room_one";

    public SyncServerTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);
        syncServer = new SyncServer(getContext(), syncano, null);

        // ----------------- Delete Channel -----------------
        // Make sure slug is not taken.
        syncano.deleteChannel(CHANNEL_NAME).send();

        // ----------------- Create Channel -----------------
        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        Response <Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateChannel.getHttpResultCode());
        assertNotNull(responseCreateChannel.getData());
        channel = responseCreateChannel.getData();

        // Make sure class exists.
        String className = Syncano.getSyncanoClassName(TestSyncanoClass.class);
        Response<SyncanoClass> responseGetSyncanoClass = syncano.getSyncanoClass(className).send();

        if (responseGetSyncanoClass.getHttpResultCode() == Response.HTTP_CODE_NOT_FOUND) {
            SyncanoClass syncanoClass = new SyncanoClass(className, TestSyncanoClass.getSchema());
            Response<SyncanoClass> responseCreateClass = syncano.createSyncanoClass(syncanoClass).send();
            assertEquals(responseCreateClass.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateClass.getHttpResultCode());
        }
    }

    @Override
    protected void tearDown() throws Exception {
        // ----------------- Delete Channel -----------------
        Response <Channel> responseDeleteChannel = syncano.deleteChannel(channel.getName()).send();
        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteChannel.getHttpResultCode());
    }

    public void testSyncServer() throws InterruptedException {

        syncServer.setSyncServerListener(new SyncServer.SyncServerListener() {
            @Override
            public void onMessage(Notification notification) {
                Log.d(TAG, "onMessage: id= " + notification.getId());
                lock.countDown();
            }

            @Override
            public void onError(Response<Notification> response) {
                Log.d(TAG, "onError: " + response.toString());
                fail("Error response: " + response.toString());
            }
        });

        int pushCount = 10;
        lock = new CountDownLatch(pushCount);
        syncServer.start(channel.getName(), ROOM_NAME);
        Thread.sleep(100); //Wait for thread to start.

        // ----------------- Publish Notification -----------------

        for (int i = 0; i < pushCount; i++) {
            JsonObject payload = new JsonObject();
            payload.addProperty("my_property", "my_value");

            final Notification newNotification = new Notification(ROOM_NAME, payload);
            Response <Notification> responsePublish = syncano.publishOnChannel(channel.getName(), newNotification).send();

            assertEquals(responsePublish.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responsePublish.getHttpResultCode());
            assertNotNull(responsePublish.getData());
        }

        /*for (int i = 0; i < messageCount; i++) {
            final TestSyncanoClass testObjectOne = new TestSyncanoClass();
            testObjectOne.setChannel(channel.getName());
            testObjectOne.setChannelRoom(ROOM_NAME);

            Response <TestSyncanoClass> responseCreateObjectOne = syncano.createObject(testObjectOne).send();

            assertEquals(responseCreateObjectOne.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateObjectOne.getHttpResultCode());
            assertNotNull(responseCreateObjectOne.getData());
        }*/

        lock.await(WAITING_MILLIS, TimeUnit.MILLISECONDS);
        syncServer.stop();
    }
}