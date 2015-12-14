package com.syncano.library.tests;

import android.util.Log;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoObject;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelPermissions;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.choice.NotificationAction;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ChannelConnectionTest extends SyncanoApplicationTestCase {

    private static final String TAG = ChannelConnectionTest.class.getSimpleName();
    private CountDownLatch lock;
    private static final String CHANNEL_NAME = "channel_one";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // ----------------- Delete Channel -----------------
        // Make sure slug is not taken.
        Response<Channel> resp = syncano.deleteChannel(CHANNEL_NAME).send();
        assertTrue(resp.isSuccess());

        // Make sure class exists.
        createClass(TestSyncanoObject.class);
    }

    @Override
    protected void tearDown() throws Exception {
        Response<Channel> responseDeleteChannel = syncano.deleteChannel(CHANNEL_NAME).send();
        assertTrue(responseDeleteChannel.isSuccess());
    }

    private void customPublishCheck(String room) throws InterruptedException {
        // ----------------- Create Channel -----------------
        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        if (room == null) {
            newChannel.setType(ChannelType.DEFAULT);
        } else {
            newChannel.setType(ChannelType.SEPARATE_ROOMS);
        }
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertTrue(responseCreateChannel.isSuccess());
        Channel channel = responseCreateChannel.getData();
        assertNotNull(channel);

        // ----------------- Listen to notifications -----------------

        final AtomicInteger notificationsReceived = new AtomicInteger(0);
        ChannelConnection channelConnection = new ChannelConnection(syncano);
        channelConnection.setChannelConnectionListener(new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {
                Log.d(TAG, "onNotification: id= " + notification.getId());
                assertEquals(notification.getAction(), NotificationAction.CUSTOM);
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
        channelConnection.start(channel.getName(), room);
        Thread.sleep(1000); //Wait for connection to start.

        // ----------------- Publish Notifications -----------------

        for (int i = 0; i < notificationsToSend; i++) {
            JsonObject payload = new JsonObject();
            payload.addProperty("my_property", "my_value");

            Notification newNotification = new Notification(room, payload);
            Response<Notification> responsePublish = syncano.publishOnChannel(channel.getName(), newNotification).send();
            assertTrue(responsePublish.isSuccess());
            assertNotNull(responsePublish.getData());
        }

        lock.await(120, TimeUnit.SECONDS);
        channelConnection.stop();

        assertEquals(notificationsToSend, notificationsReceived.get());
    }

    private void dataChangesCheck(String room) throws InterruptedException {
        // ----------------- Create Channel -----------------
        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(false);
        if (room == null) {
            newChannel.setType(ChannelType.DEFAULT);
        } else {
            newChannel.setType(ChannelType.SEPARATE_ROOMS);
        }
        newChannel.setOtherPermissions(ChannelPermissions.SUBSCRIBE);
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertTrue(responseCreateChannel.isSuccess());
        Channel channel = responseCreateChannel.getData();
        assertNotNull(channel);

        // ----------------- Listen to notifications -----------------

        final AtomicBoolean createReceived = new AtomicBoolean(false);
        final AtomicBoolean updateReceived = new AtomicBoolean(false);
        final AtomicBoolean removeReceived = new AtomicBoolean(false);

        ChannelConnection channelConnection = new ChannelConnection(syncano);
        channelConnection.setChannelConnectionListener(new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {
                Log.d(TAG, "onNotification: id= " + notification.getId() + " " + notification.getAction());
                if (notification.getAction() == NotificationAction.CREATE) {
                    createReceived.set(true);
                } else if (notification.getAction() == NotificationAction.UPDATE) {
                    updateReceived.set(true);
                } else if (notification.getAction() == NotificationAction.DELETE) {
                    removeReceived.set(true);
                }
                lock.countDown();
            }

            @Override
            public void onError(Response<Notification> response) {
                Log.d(TAG, "onError: " + response.toString());
                fail("Error response: " + response.toString());
            }
        });

        int notificationsToSend = 3;
        lock = new CountDownLatch(notificationsToSend);
        channelConnection.start(channel.getName(), room);
        Thread.sleep(1000); //Wait for connection to start.

        // ----------------- Publish changes -----------------

        // create
        TestSyncanoObject testObject = new TestSyncanoObject();
        testObject.valueOne = "val1";
        testObject.setChannel(CHANNEL_NAME);
        testObject.setChannelRoom(room);
        Response<TestSyncanoObject> responseCreate = syncano.createObject(testObject).send();
        assertTrue(responseCreate.isSuccess());
        TestSyncanoObject returnedObject = responseCreate.getData();
        assertNotNull(returnedObject);
        assertEquals(returnedObject.valueOne, testObject.valueOne);

        // update
        testObject.setId(returnedObject.getId());
        testObject.valueOne = "other val1";
        Response<TestSyncanoObject> responseUpdate = syncano.updateObject(testObject).send();
        assertTrue(responseUpdate.isSuccess());
        returnedObject = responseUpdate.getData();
        assertNotNull(returnedObject);
        assertEquals(returnedObject.valueOne, testObject.valueOne);

        // delete
        Response<TestSyncanoObject> responseDelete = syncano.deleteObject(TestSyncanoObject.class, returnedObject.getId()).send();
        assertTrue(responseDelete.isSuccess());


        // Wait for notifications
        lock.await(60, TimeUnit.SECONDS);
        channelConnection.stop();

        assertTrue(createReceived.get());
        assertTrue(updateReceived.get());
        assertTrue(removeReceived.get());
    }

    public void testDataChanges() throws InterruptedException {
        dataChangesCheck(null);
    }

    public void testDataChangesInRoom() throws InterruptedException {
        dataChangesCheck("custom_room_name");
    }

    public void testCustomPublish() throws InterruptedException {
        customPublishCheck(null);
    }

    public void testCustomPublishInRoom() throws InterruptedException {
        customPublishCheck("other_custom_room_name");
    }
}