package com.syncano.library.documentation;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelPermissions;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;

import java.util.List;

public class RealtimeCommunication extends SyncanoApplicationTestCase {

    private void createChannel(String name, ChannelType type, boolean customPublish) {
        syncano.deleteChannel(name).send();
        final Channel newChannel = new Channel(name);
        newChannel.setType(type);
        newChannel.setOtherPermissions(ChannelPermissions.SUBSCRIBE);
        newChannel.setCustomPublish(customPublish);
        syncano.createChannel(newChannel).send();
    }

    private void deleteChannel(String name) {
        syncano.deleteChannel(name).send();
    }

    private void createClass(SyncanoClass clazz) {
        syncano.deleteSyncanoClass(clazz.getName()).send();
        Response<com.syncano.library.data.SyncanoClass> responseCreateClass = syncano.createSyncanoClass(clazz).send();
        assertEquals(responseCreateClass.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responseCreateClass.getHttpResultCode());
        assertNotNull(responseCreateClass.getData());
    }

    public void testCreateChannel() {
        deleteChannel("channel_name");

        // ---------- Creating a Channel
        Channel newChannel = new Channel("channel_name");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.SUBSCRIBE);
        newChannel.setCustomPublish(false);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
        assertNotNull(response.getData());
    }

    public void testPoll() {
        createChannel("channel_name", ChannelType.DEFAULT, false);

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
        SyncanoClass syncanoClass = new SyncanoClass("Book", new JsonArray());
        createClass(syncanoClass);
        createChannel("channel_name", ChannelType.DEFAULT, false);

        // ---------- Creating a Data Object
        Book book = new Book();
        book.setChannel("channel_name");

        Response<Book> response = syncano.createObject(book).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testHandlePoll() {
        createChannel("channel_name", ChannelType.DEFAULT, false);
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
        deleteChannel("can_publish");

        // ---------- Creating a Channel with the custom_publish flag
        Channel newChannel = new Channel("can_publish");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
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
        createChannel("can_publish", ChannelType.DEFAULT, true);

        // ---------- Sending custom messages
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "hello!");

        Notification newNotification = new Notification(payload);
        Response<Notification> response = syncano.publishOnChannel("can_publish", newNotification).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testRooms() {
        syncano.deleteChannel("channel_with_rooms").send();

        // ---------- Creating separate_rooms channel
        Channel newChannel = new Channel("channel_with_rooms");
        newChannel.setType(ChannelType.SEPARATE_ROOMS);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());

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
        createChannel("separate_rooms", ChannelType.SEPARATE_ROOMS, true);

        // ---------- Creating Data Objects connected to the separate_rooms channel
        final Book book = new Book();
        book.setChannel("separate_rooms");
        book.setChannelRoom("room_name");
        Response<Book> response = syncano.createObject(book).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testCustomNotification() {
        createChannel("separate_rooms", ChannelType.SEPARATE_ROOMS, true);

        // ---------- Publishing custom messages to the separate_rooms channel
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "hello!");

        Notification newNotification = new Notification("room_name", payload);
        Response<Notification> response = syncano.publishOnChannel("separate_rooms", newNotification).send();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_CREATED, response.getHttpResultCode());
    }

    public void testHistory() {
        createChannel("channel_name", ChannelType.DEFAULT, true);

        // ---------- User viewing a Channel history
        Response<List<Notification>> response = syncano.getChannelsHistory("channel_name").send();

        List<Notification> list = response.getData();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }

    public void testHistoryInRoom() {
        createChannel("channel_name", ChannelType.SEPARATE_ROOMS, true);

        // ---------- User viewing history of a Channel with "separate_rooms`
        Response<List<Notification>> response = syncano.getChannelsHistory("channel_name", "room_name").send();

        List<Notification> list = response.getData();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }

    public void testAdminHistoryInRoom() {
        createChannel("channel_name", ChannelType.SEPARATE_ROOMS, true);

        // ---------- User viewing history of a Channel with "separate_rooms`
        Response<List<Notification>> response = syncano.getChannelsHistory("channel_name", "room_name").send();

        List<Notification> list = response.getData();
        // -----------------------------

        assertEquals(Response.HTTP_CODE_SUCCESS, response.getHttpResultCode());
    }

    @com.syncano.library.annotation.SyncanoClass(name = "Book")
    private static class Book extends SyncanoObject {
    }

}