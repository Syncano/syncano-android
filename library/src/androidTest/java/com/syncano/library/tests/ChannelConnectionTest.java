package com.syncano.library.tests;

import android.util.Log;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoClass;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ChannelConnectionTest extends SyncanoApplicationTestCase {

    private static final String TAG = ChannelConnectionTest.class.getSimpleName();

    private Channel channel;
    private CountDownLatch lock;

    private static final String CHANNEL_NAME = "channel_one";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // ----------------- Delete Channel -----------------
        // Make sure slug is not taken.
        syncano.deleteChannel(CHANNEL_NAME).send();

        // ----------------- Create Channel -----------------
        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

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
        Response<Channel> responseDeleteChannel = syncano.deleteChannel(channel.getName()).send();
        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteChannel.getHttpResultCode());
    }

    public void testChannelConnection() throws InterruptedException {
        final AtomicInteger notificationsReceived = new AtomicInteger(0);
        ChannelConnection channelConnection = new ChannelConnection(syncano);
        channelConnection.setChannelConnectionListener(new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {
                Log.d(TAG, "onNotification: id= " + notification.getId());
                notificationsReceived.incrementAndGet();
                lock.countDown();
            }

            @Override
            public void onError(Response<Notification> response) {
                Log.d(TAG, "onError: " + response.toString());
                fail("Error response: " + response.toString());
            }
        });

        int notificationsToSend = 10;
        lock = new CountDownLatch(notificationsToSend);
        channelConnection.start(channel.getName());
        Thread.sleep(1000); //Wait for connection to start.

        // ----------------- Publish Notifications -----------------

        for (int i = 0; i < notificationsToSend; i++) {
            JsonObject payload = new JsonObject();
            payload.addProperty("my_property", "my_value");

            final Notification newNotification = new Notification(payload);
            Response<Notification> responsePublish = syncano.publishOnChannel(channel.getName(), newNotification).send();

            assertEquals(responsePublish.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responsePublish.getHttpResultCode());
            assertNotNull(responsePublish.getData());
        }

        lock.await(120, TimeUnit.SECONDS);
        channelConnection.stop();

        assertEquals(notificationsToSend, notificationsReceived.get());
    }
}