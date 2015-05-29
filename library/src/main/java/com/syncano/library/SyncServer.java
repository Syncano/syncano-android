package com.syncano.library;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.syncano.library.api.Response;
import com.syncano.library.data.Notification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncServer {

    private static final String TAG = SyncServer.class.getSimpleName();
    private static final int ERROR_DELAY = 1000;
    private static final int MSG_SUCCESS = 1;
    private static final int MSG_ERROR = 2;

    private Context context;
    private Syncano syncano;
    private Handler handler;
    private SyncServerListener syncServerListener;
    private String channel;
    private String room;
    private int lastId;

    private boolean isRunning;
    private boolean hasError;
    protected ExecutorService requestExecutor = Executors.newFixedThreadPool(1);

    public SyncServer(Context context, Syncano syncano, SyncServerListener syncServerListener) {
        this.syncano = syncano;
        this.syncServerListener = syncServerListener;
        this.context = context;
        handler = new ListenerHandler(Looper.getMainLooper());
    }

    public void start(String channel, String room) {
        start(channel, room, 0);
    }

    public void start(String channel, String room, int lastId) {
        this.channel = channel;
        this.room = room;
        this.lastId = lastId;

        if (BuildConfig.DEBUG) Log.d(TAG, "start channel: " + channel + " room: " + room + " lastId: " + lastId);
        isRunning = true;
        requestExecutor.execute(pollRequestLoop);
    }

    public void stop() {
        if (BuildConfig.DEBUG) Log.d(TAG, "stop");
        isRunning = false;
        requestExecutor.shutdown();
    }

    public SyncServerListener getSyncServerListener() {
        return syncServerListener;
    }

    public void setSyncServerListener(SyncServerListener syncServerListener) {
        this.syncServerListener = syncServerListener;
    }

    private Runnable pollRequestLoop = new Runnable() {

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
            Notification notification = response.getData();
            lastId = notification.getId();

            if (syncServerListener != null) {
                Message message = handler.obtainMessage(MSG_SUCCESS, notification);
                handler.sendMessage(message);
            }
        }
    }

    private void handleError(Response<Notification> response) {

        if (syncServerListener != null) {
            Message message = handler.obtainMessage(MSG_ERROR, response);
            handler.sendMessage(message);
        }
    }

    public interface SyncServerListener {
        void onMessage(Notification notification);
        void onError(Response<Notification> response);
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
