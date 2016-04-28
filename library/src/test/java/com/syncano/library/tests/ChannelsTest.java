package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

public class ChannelsTest extends SyncanoApplicationTestCase {

    private static final String CHANNEL_NAME = "channel_one";
    private static final String FIRST_DESCRIPTION = "First description";
    private static final String SECOND_DESCRIPTION = "Second description";
    private static final String KEY = "great_key";
    private static final String VALUE = "value value";
    private static final String ROOM = "dark_room";

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // ----------------- Delete Channel -----------------
        // Make sure slug is not taken.
        syncano.deleteChannel(CHANNEL_NAME).send();
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testChannels() {

        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        newChannel.setDescription(FIRST_DESCRIPTION);
        newChannel.setType(ChannelType.DEFAULT);
        Channel channel;

        // ----------------- Create -----------------
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();
        assertTrue(responseCreateChannel.isSuccess());
        assertNotNull(responseCreateChannel.getData());
        channel = responseCreateChannel.getData();
        assertEquals(channel.getName(), CHANNEL_NAME);

        // ----------------- Get One -----------------
        Response<Channel> responseGetChannel = syncano.getChannel(channel.getName()).send();

        assertTrue(responseGetChannel.isSuccess());
        assertNotNull(responseGetChannel.getData());
        assertEquals(CHANNEL_NAME, responseGetChannel.getData().getName());
        assertEquals(FIRST_DESCRIPTION, responseGetChannel.getData().getDescription());

        // ----------------- Get List -----------------
        Response<List<Channel>> responseGetChannels = syncano.getChannels().send();

        assertNotNull(responseGetChannels.getData());
        assertTrue("List should contain at least one item.", responseGetChannels.getData().size() > 0);

        // ----------------- Update -----------------
        channel.setDescription(SECOND_DESCRIPTION);
        Response<Channel> responseUpdateChannel = syncano.updateChannel(channel).send();

        assertTrue(responseUpdateChannel.isSuccess());
        assertNotNull(responseUpdateChannel.getData());
        assertEquals(CHANNEL_NAME, responseUpdateChannel.getData().getName());
        assertEquals(SECOND_DESCRIPTION, responseUpdateChannel.getData().getDescription());

        // ----------------- Publish Notification -----------------
        JsonObject payload = new JsonObject();
        payload.addProperty(KEY, VALUE);

        final Notification newNotification = new Notification(payload);
        Response<Notification> responsePublish = syncano.publishOnChannel(CHANNEL_NAME, newNotification).send();

        assertTrue(responsePublish.isSuccess());
        assertNotNull(responsePublish.getData());
        assertNotNull(responsePublish.getData().getPayload());
        assertTrue(responsePublish.getData().getPayload().has(KEY));

        // ----------------- Get Notification History -----------------
        Response<List<Notification>> responseGetChannelsHistory = syncano.getChannelsHistory(channel.getName()).send();

        assertNotNull(responseGetChannelsHistory.getData());
        assertTrue(responseGetChannelsHistory.getData().size() == 1);
        assertNotNull(responseGetChannelsHistory.getData().get(0).getAction());
        String value = responseGetChannelsHistory.getData().get(0).getPayload().get(KEY).getAsString();
        assertTrue(VALUE.equals(value));

        // ----------------- Delete -----------------
        Response<Channel> responseDeleteChannel = syncano.deleteChannel(channel.getName()).send();

        assertTrue(responseDeleteChannel.isSuccess());
    }

    @Test
    public void testChannelWithRooms() {
        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        newChannel.setDescription(FIRST_DESCRIPTION);
        newChannel.setType(ChannelType.SEPARATE_ROOMS);
        Channel channel;

        // ----------------- Create -----------------
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertTrue(responseCreateChannel.isSuccess());
        assertNotNull(responseCreateChannel.getData());
        channel = responseCreateChannel.getData();
        assertEquals(channel.getName(), CHANNEL_NAME);

        // ----------------- Get One -----------------
        Response<Channel> responseGetChannel = syncano.getChannel(channel.getName()).send();

        assertTrue(responseGetChannel.isSuccess());
        assertNotNull(responseGetChannel.getData());
        assertEquals(CHANNEL_NAME, responseGetChannel.getData().getName());
        assertEquals(FIRST_DESCRIPTION, responseGetChannel.getData().getDescription());

        // ----------------- Publish Notification -----------------
        JsonObject payload = new JsonObject();
        payload.addProperty(KEY, VALUE);

        Notification newNotification = new Notification(ROOM, payload);
        Response<Notification> responsePublish = syncano.publishOnChannel(CHANNEL_NAME, newNotification).send();

        assertTrue(responsePublish.isSuccess());
        assertNotNull(responsePublish.getData());
        assertNotNull(responsePublish.getData().getPayload());
        assertTrue(responsePublish.getData().getPayload().has(KEY));

        // ----------------- Get Notification History -----------------
        Response<List<Notification>> responseGetChannelsHistory = syncano.getChannelsHistory(channel.getName(), ROOM).send();

        assertNotNull(responseGetChannelsHistory.getData());
        assertTrue(responseGetChannelsHistory.getData().size() == 1);
        assertNotNull(responseGetChannelsHistory.getData().get(0).getAction());
        String value = responseGetChannelsHistory.getData().get(0).getPayload().get(KEY).getAsString();
        assertTrue(VALUE.equals(value));

        // ----------------- Delete -----------------
        Response<Channel> responseDeleteChannel = syncano.deleteChannel(channel.getName()).send();

        assertTrue(responseDeleteChannel.isSuccess());
    }
}