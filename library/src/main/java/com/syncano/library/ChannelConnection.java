package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;
import com.syncano.library.utils.SyncanoLog;

/**
 * Class responsible for real time communication.
 */
public class ChannelConnection {

    private static final String TAG = ChannelConnection.class.getSimpleName();

    private Syncano syncano;
    private ChannelConnectionListener channelConnectionListener;
    private PollRequestLoop pollRequestLoop;
    private String channel;
    private String room;
    private int lastId;

    public ChannelConnection(Syncano syncano) {
        this(syncano, null);
    }

    public ChannelConnection(Syncano syncano, ChannelConnectionListener channelConnectionListener) {
        this.syncano = syncano;
        this.channelConnectionListener = channelConnectionListener;
    }

    /**
     * Start poll request loop.
     *
     * @param channel Channel to listen to.
     */
    public void start(String channel) {
        start(channel, null, 0);
    }

    /**
     * Start poll request loop.
     *
     * @param channel Channel to listen to.
     * @param room    Room to listen to.
     */
    public void start(String channel, String room) {
        start(channel, room, 0);
    }

    /**
     * Start poll request loop.
     *
     * @param channel Channel to listen to.
     * @param room    Room to listen to.
     * @param lastId  Last notification id.
     */
    public void start(String channel, String room, int lastId) {
        this.channel = channel;
        this.room = room;
        this.lastId = lastId;

        if (BuildConfig.DEBUG)
            SyncanoLog.d(TAG, "start channel: " + channel + " room: " + room + " lastId: " + lastId);

        if (pollRequestLoop != null) {
            pollRequestLoop.stop();
        }
        pollRequestLoop = new PollRequestLoop();
        new Thread(pollRequestLoop).start();
    }

    /**
     * Stop poll request loop.
     * This method should be called always when start was invoked.
     * Good place to call it is onPause or onStop method from Activity/Fragment lifecycle.
     */
    public void stop() {
        if (BuildConfig.DEBUG) SyncanoLog.d(TAG, "stop");

        if (pollRequestLoop != null) {
            pollRequestLoop.stop();
            pollRequestLoop = null;
        }
    }

    /**
     * Get ChannelConnectionListener.
     *
     * @return ChannelConnectionListener.
     */
    public ChannelConnectionListener getChannelConnectionListener() {
        return channelConnectionListener;
    }

    /**
     * Set ChannelConnectionListener. Method might be used for changing listener for new one
     * or for setting it if null was set in constructor.
     *
     * @param channelConnectionListener New ChannelConnectionListener.
     */
    public void setChannelConnectionListener(ChannelConnectionListener channelConnectionListener) {
        this.channelConnectionListener = channelConnectionListener;
    }

    private void handleSuccess(final Response<Notification> response) {
        if (response.getData() != null) {
            lastId = response.getData().getId();

            if (channelConnectionListener != null) {
                PlatformType.get().runOnCallbackThread(new Runnable() {
                    @Override
                    public void run() {
                        channelConnectionListener.onNotification(response.getData());
                    }
                });
            }
        }
    }

    private void handleError(final Response<Notification> response) {
        if (channelConnectionListener != null) {
            PlatformType.get().runOnCallbackThread(new Runnable() {
                @Override
                public void run() {
                    channelConnectionListener.onError(response);
                }
            });
        }
    }

    private class PollRequestLoop implements Runnable {
        private boolean isRunning = true;

        public void stop() {
            isRunning = false;
        }

        public boolean isRunning() {
            return isRunning;
        }

        @Override
        public void run() {
            while (isRunning) {
                if (BuildConfig.DEBUG)
                    SyncanoLog.d(TAG, "poll request channel: " + channel + " room: " + room + " lastId: " + lastId);
                Response<Notification> responsePollFromChannel = syncano.pollChannel(channel, room, lastId).send();
                if (BuildConfig.DEBUG) SyncanoLog.d(TAG, "response: " + responsePollFromChannel);

                // If still running, handle response.
                if (!isRunning) {
                    break;
                }
                int respCode = responsePollFromChannel.getHttpResultCode();
                if (respCode == Response.HTTP_CODE_SUCCESS) {
                    handleSuccess(responsePollFromChannel);
                } else if (respCode == Response.HTTP_CODE_NO_CONTENT || respCode == Response.HTTP_CODE_GATEWAY_TIMEOUT) {
                    //long polling timeout, just continue the loop
                } else {
                    handleError(responsePollFromChannel);
                    isRunning = false;
                    break;
                }
            }
        }
    }
}
