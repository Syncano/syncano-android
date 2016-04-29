package com.syncano.library.documentation;

import com.google.gson.JsonObject;
import com.syncano.library.ChannelConnection;
import com.syncano.library.ChannelConnectionListener;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.api.ResponseGetList;
import com.syncano.library.choice.ChannelPermissions;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;
import com.syncano.library.model.Author;
import com.syncano.library.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class Channels extends SyncanoApplicationTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void createChannel() throws InterruptedException {
        createClass(Author.class);
        createClass(Book.class);
        //
        Channel newChannel = new Channel("channel_name");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.SUBSCRIBE);
        newChannel.setCustomPublish(false);

        Response<Channel> response = syncano.createChannel(newChannel).send();
        //

        //
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {
            }

            @Override
            public void onError(Response<Notification> response) {
            }
        });

        channelConnection.start("channel_name");
        //
        channelConnection.stop();

        //
        Book book = new Book();
        book.setChannel("channel_name");
        book.save();
        //

        int lastId = 1;
        //
        channelConnection.start("channel_name", null, lastId);
        //
    }

    @Test
    public void createCustomPublishChannel() throws InterruptedException {
        //
        Channel newChannel = new Channel("channel_name");
        newChannel.setType(ChannelType.DEFAULT);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);

        syncano.createChannel(newChannel).send();
        //

        //
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {
            }

            @Override
            public void onError(Response<Notification> response) {
            }
        });

        channelConnection.start("channel_name");
        //

        //
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "hello!");

        Notification newNotification = new Notification(payload);
        Response<Notification> response = syncano.publishOnChannel("channel_name", newNotification).send();
        //
    }

    @Test
    public void createChannelRooms() throws InterruptedException {
        //
        Channel newChannel = new Channel("channel_with_rooms");
        newChannel.setType(ChannelType.SEPARATE_ROOMS);
        newChannel.setOtherPermissions(ChannelPermissions.PUBLISH);
        newChannel.setCustomPublish(true);

        syncano.createChannel(newChannel).send();
        //

        //
        ChannelConnection channelConnection = new ChannelConnection(syncano, new ChannelConnectionListener() {
            @Override
            public void onNotification(Notification notification) {
            }

            @Override
            public void onError(Response<Notification> response) {
            }
        });

        channelConnection.start("channel_with_rooms", "room_name");
        //

        //
        Book book = new Book();
        book.setChannel("separate_rooms");
        book.setChannelRoom("room_name");
        book.save();
        //

        //
        JsonObject payload = new JsonObject();
        payload.addProperty("content", "hello!");

        Notification newNotification = new Notification("room_name", payload);
        syncano.publishOnChannel("separate_rooms", newNotification).send();
        //

        //
        ResponseGetList<Notification> response = syncano.getChannelsHistory("channel_name").send();
        List<Notification> list = response.getData();
        //

        //
        ResponseGetList<Notification> roomResponse = syncano.getChannelsHistory("channel_name", "room_name").send();
        List<Notification> roomList = roomResponse.getData();
        //
    }
}
