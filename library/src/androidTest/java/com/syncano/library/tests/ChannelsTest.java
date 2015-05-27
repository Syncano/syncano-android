package com.syncano.library.tests;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.syncano.library.Config;
import com.syncano.library.Syncano;
import com.syncano.library.api.Response;
import com.syncano.library.choice.ChannelType;
import com.syncano.library.data.Channel;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ChannelsTest extends ApplicationTestCase<Application> {

    private static final String TAG = ChannelsTest.class.getSimpleName();

    private Syncano syncano;

    private static final String CHANNEL_NAME = "channel_one";

    public ChannelsTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        syncano = new Syncano(Config.API_KEY, Config.INSTANCE_NAME);

        // ----------------- Delete Channel -----------------
        // Make sure slug is not taken.
        syncano.deleteChannel(CHANNEL_NAME).send();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWebhooks() throws InterruptedException {

        final Channel newChannel = new Channel(CHANNEL_NAME);
        Channel channel;

        // ----------------- Create -----------------
        Response <Channel> responseCreateChannel = syncano.createChannel(newChannel).send();

        assertEquals(Response.HTTP_CODE_CREATED, responseCreateChannel.getHttpResultCode());
        assertNotNull(responseCreateChannel.getData());
        channel = responseCreateChannel.getData();

        // ----------------- Get One -----------------
        Response <Channel> responseGetChannel = syncano.getChannel(CHANNEL_NAME).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseGetChannel.getHttpResultCode());
        assertNotNull(responseGetChannel.getData());
        assertEquals(channel.getName(), responseGetChannel.getData().getName());

        // ----------------- Update -----------------
        channel.setType(ChannelType.SEPARATE_ROOMS);
        Response <Channel> responseUpdateChannel = syncano.updateChannel(channel).send();

        assertEquals(Response.HTTP_CODE_SUCCESS, responseUpdateChannel.getHttpResultCode());
        assertNotNull(responseUpdateChannel.getData());
        assertEquals(channel.getName(), responseUpdateChannel.getData().getName());
        assertEquals(channel.getType(), responseUpdateChannel.getData().getType());

        // ----------------- Get List -----------------
        Response <List<Channel>> responseGetChannels = syncano.getChannels().send();

        assertNotNull(responseGetChannels.getData());
        assertTrue("List should contain at least one item.", responseGetChannels.getData().size() > 0);


        // ----------------- Delete -----------------
        Response <Channel> responseDeleteChannel = syncano.deleteChannel(CHANNEL_NAME).send();

        assertEquals(Response.HTTP_CODE_NO_CONTENT, responseDeleteChannel.getHttpResultCode());
    }
}