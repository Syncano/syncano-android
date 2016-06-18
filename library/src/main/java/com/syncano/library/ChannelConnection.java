package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;
import com.syncano.library.utils.SyncanoLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private boolean autoReconnect = false;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private int currentLoopId = 0;

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

        if (BuildConfig.IS_DEBUG)
            SyncanoLog.d(TAG, "start channel: " + channel + " room: " + room + " lastId: " + lastId);

        if (pollRequestLoop != null) {
            pollRequestLoop.stop();
        }
        pollRequestLoop = new PollRequestLoop(currentLoopId++);
        executor.schedule(pollRequestLoop, 0, TimeUnit.SECONDS);
    }

    /**
     * Stop poll request loop.
     * This method should be called always when start was invoked.
     * Good place to call it is onPause or onStop method from Activity/Fragment lifecycle.
     */
    public void stop() {
        if (BuildConfig.IS_DEBUG) SyncanoLog.d(TAG, "stop");
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

    public boolean isAutoReconnect() {
        return autoReconnect;
    }

    /**
     * In normal case, when channel connection returns an error it stops the connection.
     * When set this to true, it will try to reconnect after 5 second. It may result in
     * messaging about an error every 5 seconds when for example internet connection is lost.
     *
     * @param autoReconnect true if should auto reconnect
     */
    public void setAutoReconnect(boolean autoReconnect) {
        this.autoReconnect = autoReconnect;
    }

    private class ConnectionStart implements Runnable {
        // keeps loop id, because other loop may be started in the meantime
        private int loopId;

        public ConnectionStart(int loopId) {
            this.loopId = loopId;
        }

        @Override
        public void run() {
            // not stopped
            if (pollRequestLoop != null && pollRequestLoop.id == loopId)
                start(channel, room, lastId);
        }
    }

    private class PollRequestLoop implements Runnable {
        private boolean isRunning = true;
        private int id;

        public PollRequestLoop(int id) {
            this.id = id;
        }

        public void stop() {
            isRunning = false;
        }

        public boolean isRunning() {
            return isRunning;
        }

        @Override
        public void run() {
            while (isRunning) {
                if (BuildConfig.IS_DEBUG)
                    SyncanoLog.d(TAG, "poll request channel: " + channel + " room: " + room + " lastId: " + lastId);
                Response<Notification> responsePollFromChannel = syncano.pollChannel(channel, room, lastId).send();
                if (BuildConfig.IS_DEBUG) SyncanoLog.d(TAG, "response: " + responsePollFromChannel);

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
                    if (autoReconnect) {
                        if (BuildConfig.IS_DEBUG) SyncanoLog.d(TAG, "Reconnecting in 5 seconds");
                        executor.schedule(new ConnectionStart(id), 5, TimeUnit.SECONDS);
                    }
                    break;
                }
            }
        }
    }
}
