package com.syncano.library;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;
import com.syncano.library.utils.SyncanoLog;

/**
 * Class responsible for real time communication.
 */
public class ChannelConnection {

    private static final String TAG = ChannelConnection.class.getSimpleName();
    private static final int ERROR_DELAY = 1000;
    private static final int MSG_SUCCESS = 1;
    private static final int MSG_ERROR = 2;
    private final ListenerHandler handler;

    private Syncano syncano;
    private ChannelConnectionListener channelConnectionListener;
    private PollRequestLoop pollRequestLoop;
    private String channel;
    private String room;
    private int lastId;

    private boolean hasError;

    public ChannelConnection(Syncano syncano) {
        this(syncano, null);
    }

    public ChannelConnection(Syncano syncano, ChannelConnectionListener channelConnectionListener) {
        this.syncano = syncano;
        this.channelConnectionListener = channelConnectionListener;
        this.handler = new ListenerHandler();
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

        if (pollRequestLoop == null) {
            pollRequestLoop = new PollRequestLoop();
            pollRequestLoop.setIsRunning(true);
            new Thread(pollRequestLoop).start();
        }
    }

    /**
     * Stop poll request loop.
     * This method should be called always when start was invoked.
     * Good place to call it is onPause or onStop method from Activity/Fragment lifecycle.
     */
    public void stop() {
        if (BuildConfig.DEBUG) SyncanoLog.d(TAG, "stop");

        if (pollRequestLoop != null) {
            pollRequestLoop.setIsRunning(false);
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

    private void handleSuccess(Response<Notification> response) {

        if (response.getData() != null) {
            lastId = response.getData().getId();

            if (channelConnectionListener != null) {
                ListenerHandler.ChannelMessage message = handler.obtainMessage(MSG_SUCCESS, response.getData());
                handler.sendMessage(message);
            }
        }
    }

    private void handleError(Response<Notification> response) {

        if (response.getHttpResultCode() == Response.HTTP_CODE_GATEWAY_TIMEOUT) {
            // If timeout, start new poll in pollRequestLoop.
            return;
        }

        if (channelConnectionListener != null) {
            ListenerHandler.ChannelMessage message = handler.obtainMessage(MSG_ERROR, response);
            handler.sendMessage(message);
        }
    }

    private class PollRequestLoop implements Runnable {

        private boolean isRunning;

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {

            while (isRunning) {
                if (BuildConfig.DEBUG)
                    SyncanoLog.d(TAG, "poll request channel: " + channel + " room: " + room + " lastId: " + lastId);
                Response<Notification> responsePollFromChannel = syncano.pollChannel(channel, room, lastId).send();
                if (BuildConfig.DEBUG) SyncanoLog.d(TAG, "response: " + responsePollFromChannel);

                // If still running, handle response.
                if (isRunning) {
                    if (responsePollFromChannel.getHttpResultCode() == Response.HTTP_CODE_NO_CONTENT) {
                        // No content - long polling timeout
                        hasError = false;
                    }
                    if (responsePollFromChannel.getHttpResultCode() == Response.HTTP_CODE_SUCCESS) {
                        handleSuccess(responsePollFromChannel);
                        hasError = false;
                    } else {
                        // Handle error only once
                        if (!hasError) {
                            handleError(responsePollFromChannel);
                            hasError = true;
                        }

                        try {
                            Thread.sleep(ERROR_DELAY);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private class ListenerHandler {


        private ListenerHandler() {
        }

        public ChannelMessage obtainMessage(int msgSuccess, Object data) {
            return new ChannelMessage(msgSuccess, data);
        }

        public void sendMessage(final ChannelMessage msg) {
            syncano.postRunnableOnCallbackThread(new Runnable() {
                @Override
                public void run() {
                    switch (msg.getType()) {
                        case MSG_SUCCESS:
                            channelConnectionListener.onNotification((Notification) msg.getData());
                            break;
                        case MSG_ERROR:
                            channelConnectionListener.onError((Response<Notification>) msg.getData());
                            break;
                    }
                }
            });
        }

        public class ChannelMessage {
            private final int type;
            private final Object data;

            public ChannelMessage(int type, Object data) {
                this.type = type;
                this.data = data;
            }

            public int getType() {
                return type;
            }

            public Object getData() {
                return data;
            }
        }
    }
}
