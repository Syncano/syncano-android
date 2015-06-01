package com.syncano.library;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;

/**
 * Class responsible for real time communication.
 */
public class SyncServer {

    private static final String TAG = SyncServer.class.getSimpleName();
    private static final int ERROR_DELAY = 1000;
    private static final int MSG_SUCCESS = 1;
    private static final int MSG_ERROR = 2;

    private Syncano syncano;
    private Handler handler;
    private SyncServerListener syncServerListener;
    private PollRequestLoop pollRequestLoop;
    private String channel;
    private String room;
    private int lastId;

    private boolean hasError;

    public SyncServer(Syncano syncano, SyncServerListener syncServerListener) {
        this.syncano = syncano;
        this.syncServerListener = syncServerListener;
        handler = new ListenerHandler(Looper.getMainLooper());
    }

    /**
     * Start poll request loop.
     * @param channel Channel to listen to.
     * @param room Room to listen to.
     */
    public void start(String channel, String room) {
        start(channel, room, 0);
    }

    /**
     * Start poll request loop.
     * @param channel Channel to listen to.
     * @param room Room to listen to.
     * @param lastId Last notification id.
     */
    public void start(String channel, String room, int lastId) {
        this.channel = channel;
        this.room = room;
        this.lastId = lastId;

        if (BuildConfig.DEBUG) Log.d(TAG, "start channel: " + channel + " room: " + room + " lastId: " + lastId);

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
        if (BuildConfig.DEBUG) Log.d(TAG, "stop");

        if (pollRequestLoop != null) {
            pollRequestLoop.setIsRunning(false);
            pollRequestLoop = null;
        }
    }

    /**
     * Get SyncServerListener.
     * @return SyncServerListener.
     */
    public SyncServerListener getSyncServerListener() {
        return syncServerListener;
    }

    /**
     * Set SyncServerListener. Method might be used for changing listener for new one
     * or for setting it if null was set in constructor.
     * @param syncServerListener New SyncServerListener.
     */
    public void setSyncServerListener(SyncServerListener syncServerListener) {
        this.syncServerListener = syncServerListener;
    }

    private class PollRequestLoop implements Runnable {

        private boolean isRunning;

        public void setIsRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

        @Override
        public void run() {

            while (isRunning) {
                if (BuildConfig.DEBUG) Log.d(TAG, "pollRequest channel: " + channel + " room: " + room + " lastId: " + lastId);
                Response<Notification> responsePollFromChannel = syncano.pollChannel(channel, room, lastId).send();

                // If still running, handle response.
                if (isRunning) {
                    if (responsePollFromChannel.getHttpResultCode() == Response.HTTP_CODE_SUCCESS) {
                        if (BuildConfig.DEBUG) Log.d(TAG, "success response: " + responsePollFromChannel);

                        handleSuccess(responsePollFromChannel);
                        hasError = false;
                    } else {
                        if (BuildConfig.DEBUG) Log.d(TAG, "error response: " + responsePollFromChannel);

                        // Handle error only once
                        if (hasError == false) {
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
    };

    private void handleSuccess(Response<Notification> response) {

        if (response.getData() != null) {
            lastId = response.getData().getId();

            if (syncServerListener != null) {
                Message message = handler.obtainMessage(MSG_SUCCESS, response.getData());
                handler.sendMessage(message);
            }
        }
    }

    private void handleError(Response<Notification> response) {

        if (response.getHttpResultCode() == Response.HTTP_CODE_GATEWAY_TIMEOUT) {
            // If timeout, start new poll in pollRequestLoop.
            return;
        }

        if (syncServerListener != null) {
            Message message = handler.obtainMessage(MSG_ERROR, response);
            handler.sendMessage(message);
        }
    }

    private class ListenerHandler extends Handler {

        ListenerHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SUCCESS:
                    syncServerListener.onMessage((Notification) msg.obj);
                    break;
                case MSG_ERROR:
                    syncServerListener.onError((Response<Notification>) msg.obj);
                    break;
            }
        }
    }
}
