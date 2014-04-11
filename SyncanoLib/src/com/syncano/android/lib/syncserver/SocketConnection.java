package com.syncano.android.lib.syncserver;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.syncano.android.lib.BuildConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SocketConnection {
	/** print writer to write data to socket */
	private PrintWriter mOut;
	/** reader to read data from socket */
	private BufferedReader mIn;
	/** data listener */
	private DataListener mListener;
	/** handler */
	private Handler mHandler;
	/** variable presenting actual state of socket */
	private boolean mStopped = false;
	/** socket connection */
	private Socket mSocket;

	/** socket state message */
	private final static int MSG_DISCONNECTED = 1;
	/** socket state message */
	private final static int MSG_NEW_DATA = 2;
	/** socket state message */
	private final static int MSG_CONNECTED = 3;

	private final static String lOG_TAG = SocketConnection.class.getSimpleName();
	/** read timeout variable */
	private final static int READ_TIMEOUT = 60 * 1000;
	/** read retry period variable */
	private final static int READ_RETRY_PERIOD = 1000;

	/** scheluded executor service */
	private ScheduledExecutorService readExecutor;
	/** executor service */
	private ExecutorService writeExecutor;
	/** context from application */
	private Context mCtx;

	/**
	 * Default constructor
	 * 
	 * @param ctx
	 *            Context from application
	 * @param listener
	 *            Data state listener
	 */
	public SocketConnection(Context ctx, DataListener listener) {
		mListener = listener;
		mCtx = ctx;
	}

	/**
	 * Method starts socket and creates new thread for it
	 */
	public void start() {
		// create handler to get messages in this thread
		mStopped = false;
		mHandler = new ListenerHandler(Looper.getMainLooper());

		// start read and write threads
		readExecutor = Executors.newSingleThreadScheduledExecutor();
		writeExecutor = Executors.newSingleThreadExecutor();

		// init connection
		Runnable initAction = new Runnable() {
			public void run() {
				getNewSocket(mCtx);
				// check if successfully connected
				if (mSocket == null || mIn == null || mOut == null) {
					Log.e(lOG_TAG, "Connection failed");
					stop();
					return;
				}

				Runnable r = new Runnable() {
					public void run() {
						read();
					}
				};
				readExecutor.scheduleWithFixedDelay(r, 0, READ_RETRY_PERIOD, TimeUnit.MILLISECONDS);

				Message msg = mHandler.obtainMessage();
				msg.what = MSG_CONNECTED;
				mHandler.sendMessage(msg);
			}
		};
		writeExecutor.execute(initAction);
	}

	/**
	 * Method writes line to socket
	 * 
	 * @param text
	 *            text to write
	 */
	private void writeLine(String text) {
		if (text == null) {
			Log.e(lOG_TAG, "Trying to send null. Not sending.");
			return;
		}
		if (mOut == null) {
			Log.e(lOG_TAG, "Not connected. Output stream is null.");
			stop();
			return;
		}
		if (BuildConfig.DEBUG) {
			Log.d(lOG_TAG, "Sending: " + text);
		}
		mOut.println(text);
	}

	/**
	 * Method creates new socket
	 * 
	 * @param ctx
	 *            context from application
	 */
	private void getNewSocket(Context ctx) {
		Log.d(lOG_TAG, "Connecting socket");
		try {
			mSocket = SocketCreator.createSocket(ctx);
			mSocket.setSoTimeout(READ_TIMEOUT);
			mIn = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));
			mOut = new PrintWriter(mSocket.getOutputStream(), true);
		} catch (Exception e) {
			Log.e(lOG_TAG, "Error when connecting");
			Log.e(lOG_TAG, e.getClass().getSimpleName() + " " + e.getMessage());
		}
	}

	/**
	 * Method stops connection and closes socket
	 */
	public void stop() {
		mStopped = true;
		if (readExecutor != null) {
			readExecutor.shutdown();
		}
		if (mSocket != null) {
			try {
				mSocket.close();
			} catch (IOException e) {
				Log.e(lOG_TAG, e.getMessage());
			}
		}

		Message msg = mHandler.obtainMessage();
		msg.what = MSG_DISCONNECTED;
		mHandler.sendMessage(msg);
	}

	/**
	 * Reads data from socket and hands it to handler
	 */
	private void read() {
		if (mStopped) {
			return;
		}
		if (mSocket.isClosed()) {
			getNewSocket(mCtx);
		}
		String msg = null;
		try {
			msg = mIn.readLine();
		} catch (IOException e) {
			Log.w(lOG_TAG, e.getMessage());
		}
		if (msg != null) {
			Message message = mHandler.obtainMessage();
			message.what = MSG_NEW_DATA;
			message.obj = msg;
			mHandler.sendMessage(message);
		}
	}

	/**
	 * Method sends data through socket
	 * 
	 * @param data
	 *            data to send
	 */
	public void send(final String data) {
		Runnable writeAction = new Runnable() {
			public void run() {
				writeLine(data);
			}
		};
		writeExecutor.execute(writeAction);
	}

	/**
	 * Listener for new data or connection state
	 */
	public interface DataListener {
		public void message(String data);

		public void disconnected();

		public void connected();
	}

	/**
	 * Informs listener about events in its Thread
	 */
	private class ListenerHandler extends Handler {

		ListenerHandler(Looper l) {
			super(l);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MSG_DISCONNECTED:
					mListener.disconnected();
					break;
				case MSG_NEW_DATA:
					mListener.message((String) msg.obj);
					break;
				case MSG_CONNECTED:
					mListener.connected();
					break;
			}

		}
	}
}
