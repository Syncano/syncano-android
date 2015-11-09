package com.syncano.library.tests;

import android.util.Log;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.Syncano;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.TestSyncanoClass;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelPermissions;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.choice.NotificationAction;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoClass;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
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
        if (channel != null) {
            Response<Channel> responseDeleteChannel = syncano.deleteChannel(channel.getName()).send();
            assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteChannel.getHttpResultCode());
        }
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

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateChannel.getHttpResultCode());
        channel = responseCreateChannel.getData();
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

            final Notification newNotification = new Notification(room, payload);
            Response<Notification> responsePublish = syncano.publishOnChannel(channel.getName(), newNotification).send();

            assertEquals(responsePublish.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responsePublish.getHttpResultCode());
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

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateChannel.getHttpResultCode());
        channel = responseCreateChannel.getData();
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
        TestSyncanoClass testObject = new TestSyncanoClass();
        testObject.valueOne = "val1";
        testObject.setChannel(CHANNEL_NAME);
        testObject.setChannelRoom(room);
        Response<TestSyncanoClass> responseCreate = syncano.createObject(testObject).send();
        assertEquals(responseCreate.getHttpResultCode(), Response.HTTP_CODE_CREATED);
        TestSyncanoClass returnedObject = responseCreate.getData();
        assertNotNull(returnedObject);
        assertEquals(returnedObject.valueOne, testObject.valueOne);

        // update
        testObject.setId(returnedObject.getId());
        testObject.valueOne = "other val1";
        Response<TestSyncanoClass> responseUpdate = syncano.updateObject(testObject).send();
        assertEquals(responseUpdate.getHttpResultCode(), Response.HTTP_CODE_SUCCESS);
        returnedObject = responseUpdate.getData();
        assertNotNull(returnedObject);
        assertEquals(returnedObject.valueOne, testObject.valueOne);

        // delete
        Response<TestSyncanoClass> responseDelete = syncano.deleteObject(TestSyncanoClass.class, returnedObject.getId()).send();
        assertEquals(responseDelete.getHttpResultCode(), Response.HTTP_CODE_NO_CONTENT);


        // Wait for notifications
        lock.await(60, TimeUnit.SECONDS);
        channelConnection.stop();

        assertTrue(createReceived.get());
        // TODO remove comment after fix on Syncano side
        //assertTrue(updateReceived.get());
        //assertTrue(removeReceived.get());
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