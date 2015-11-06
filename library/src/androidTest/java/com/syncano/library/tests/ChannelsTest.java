package com.syncano.library.tests;

import com.google.gson.JsonObject;
import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;
import com.syncano.library.data.Notification;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ChannelsTest extends SyncanoApplicationTestCase {

    private static final String CHANNEL_NAME = "channel_one";
    private static final String FIRST_DESCRIPTION = "First description";
    private static final String SECOND_DESCRIPTION = "Second description";
    private static final String KEY = "great_key";
    private static final String VALUE = "value value";
    private static final String ROOM = "dark_room";

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // ----------------- Delete Channel -----------------
        // Make sure slug is not taken.
        syncano.deleteChannel(CHANNEL_NAME).send();
    }

    public void testChannels() {

        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        newChannel.setDescription(FIRST_DESCRIPTION);
        newChannel.setType(ChannelType.DEFAULT);
        Channel channel;

        // ----------------- Create -----------------
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateChannel.getHttpResultCode());
        assertNotNull(responseCreateChannel.getData());
        channel = responseCreateChannel.getData();
        assertEquals(channel.getName(), CHANNEL_NAME);

        // ----------------- Get One -----------------
        Response<Channel> responseGetChannel = syncano.getChannel(channel.getName()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetChannel.getHttpResultCode());
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

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateChannel.getHttpResultCode());
        assertNotNull(responseUpdateChannel.getData());
        assertEquals(CHANNEL_NAME, responseUpdateChannel.getData().getName());
        assertEquals(SECOND_DESCRIPTION, responseUpdateChannel.getData().getDescription());

        // ----------------- Publish Notification -----------------
        JsonObject payload = new JsonObject();
        payload.addProperty(KEY, VALUE);

        final Notification newNotification = new Notification(payload);
        Response<Notification> responsePublish = syncano.publishOnChannel(CHANNEL_NAME, newNotification).send();

        assertEquals(responsePublish.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responsePublish.getHttpResultCode());
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

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteChannel.getHttpResultCode());
    }

    public void testChannelWithRooms() {
        final Channel newChannel = new Channel(CHANNEL_NAME);
        newChannel.setCustomPublish(true); // Required to pass publish test
        newChannel.setDescription(FIRST_DESCRIPTION);
        newChannel.setType(ChannelType.SEPARATE_ROOMS);
        Channel channel;

        // ----------------- Create -----------------
        Response<Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateChannel.getHttpResultCode());
        assertNotNull(responseCreateChannel.getData());
        channel = responseCreateChannel.getData();
        assertEquals(channel.getName(), CHANNEL_NAME);

        // ----------------- Get One -----------------
        Response<Channel> responseGetChannel = syncano.getChannel(channel.getName()).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetChannel.getHttpResultCode());
        assertNotNull(responseGetChannel.getData());
        assertEquals(CHANNEL_NAME, responseGetChannel.getData().getName());
        assertEquals(FIRST_DESCRIPTION, responseGetChannel.getData().getDescription());

        // ----------------- Publish Notification -----------------
        JsonObject payload = new JsonObject();
        payload.addProperty(KEY, VALUE);

        final Notification newNotification = new Notification(ROOM, payload);
        Response<Notification> responsePublish = syncano.publishOnChannel(CHANNEL_NAME, newNotification).send();

        assertEquals(responsePublish.getHttpReasonPhrase(), Response.HTTP_CODE_CREATED, responsePublish.getHttpResultCode());
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

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteChannel.getHttpResultCode());
    }
}