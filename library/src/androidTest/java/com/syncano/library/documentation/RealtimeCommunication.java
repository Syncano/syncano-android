package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelPermissions;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoObject;

public class RealtimeCommunication extends SyncanoApplicationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        syncano.deleteChannel("channel_name").send();
        final Channel newChannel = new Channel("channel_name");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.SUBSCRIBE);
        newChannel.setCustomPublish(false);
        syncano.createChannel(newChannel).send();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        syncano.deleteChannel("channel_name").send();
    }

    public void testCreateChannel() {
        syncano.deleteChannel("channel_name").send();

        // ---------- Creating a Channel
        final Channel newChannel = new Channel("channel_name");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.SUBSCRIBE);
        newChannel.setCustomPublish(false);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        // -----------------------------

        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_CREATED);
        assertNotNull(response.getData());
    }

    public void testPoll() {
        // ---------- Polling for notification messages
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {

            }

            @Override
            public void onError(Response<Notification> response) {

            }
        });

        channelConnection.start("channel_name");
        // -----------------------------

        channelConnection.stop();
    }

    public void testCreateObject() {
        // ---------- Creating a Data Object
        Book book = new Book();
        book.setChannel("channel_name");

        Response<Book> response = syncano.createObject(book).send();
        // -----------------------------

        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_CREATED);
    }

    public void testHandlePoll() {
        int lastId = 0;

        // ---------- Handling the polling requests
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {

            }

            @Override
            public void onError(Response<Notification> response) {

            }
        });

        channelConnection.start("channel_name", "room_name", lastId);
        // -----------------------------

        channelConnection.stop();
    }

    public void testCustomPublish() {
        syncano.deleteChannel("can_publish").send();

        // ---------- Creating a Channel with the custom_publish flag
        Channel newChannel = new Channel("can_publish");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        // -----------------------------

        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_CREATED);
        assertNotNull(response.getData());

        // ---------- Polling for changes
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {

            }

            @Override
            public void onError(Response<Notification> response) {

            }
        });

        channelConnection.start("can_publish");
        // -----------------------------

        channelConnection.stop();

    }

    public void testSendCustomNotification() {
        syncano.deleteChannel("can_publish").send();
        Channel newChannel = new Channel("can_publish");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);
        syncano.createChannel(newChannel).send();

        // ---------- Sending custom messages
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "hello!");

        Notification newNotification = new Notification(null, payload);
        Response<Notification> response = syncano.publishOnChannel("can_publish", newNotification).send();
        // -----------------------------

        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_SUCCESS);
    }

    public void testRooms() {
        syncano.deleteChannel("channel_with_rooms").send();

        // ---------- Sending custom messages
        Channel newChannel = new Channel("channel_with_rooms");
        newChannel.setType(ChannelType.SEPARATE_ROOMS);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        // -----------------------------

        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_CREATED);

        // ---------- Polling for changes in a room
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {

            }

            @Override
            public void onError(Response<Notification> response) {

            }
        });

        channelConnection.start("channel_with_rooms", "room_name");
        // -----------------------------

        channelConnection.stop();
    }

    public void testCreateObjectInRoom() {
        syncano.deleteChannel("separate_rooms").send();
        Channel newChannel = new Channel("separate_rooms");
        newChannel.setType(ChannelType.SEPARATE_ROOMS);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);
        syncano.createChannel(newChannel).send();

        // ---------- Creating Data Objects connected to the separate_rooms channel
        final Book book = new Book();
        book.setChannel("separate_rooms");
        book.setChannelRoom("room_name");
        Response<Book> response = syncano.createObject(book).send();
        // -----------------------------

        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_CREATED);
    }

    public void testCustomNotification() {
        // ---------- Publishing custom messages to the separate_rooms channel
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "hello!");

        final Notification newNotification = new Notification("room_name", payload);
        Response<Notification> response = syncano.publishOnChannel("separate_rooms", newNotification).send();
        // -----------------------------
        assertEquals(response.getHttpResultCode(), Response.HTTP_CODE_SUCCESS);


    }

    private static class Book extends SyncanoObject {
    }

}