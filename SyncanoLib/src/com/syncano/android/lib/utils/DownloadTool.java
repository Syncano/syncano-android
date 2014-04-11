package com.syncano.android.lib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.syncano.android.lib.BuildConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Used to easy download files.
 */
public class DownloadTool {

	private final static String LOG_TAG = DownloadTool.class.getSimpleName();

	/**
	 * Returns a byte array, witch is file downloaded from specified url. If downloading fails, it tries again until
	 * limit reached. It doesn't create new thread. It doesn't throw exceptions, but may return null or corrupted data.
	 * 
	 * @param context
	 *            application context
	 * @param url
	 *            http url to file
	 * @param stopper
	 *            used to stop download, may be null
	 * @param postData
	 *            may be null
	 * @param tries
	 *            connection attempts, must be at least 1
	 * @param conTimeout
	 *            millis timeout for one connection, 0 means default
	 * @return byte[] containing downloaded file
	 * 
	 */
	public static byte[] download(Context context, String url, Stopper stopper, String postData, int tries,
			int conTimeout) {
		int tryNum = 0;
		byte[] data = null;
		while (data == null && tryNum < tries) {
			if (stopper != null && stopper.isStopped()) return null;
			data = download(context, url, stopper, postData, conTimeout);
			tryNum++;
		}
		return data;
	}

	/**
	 * Returns a byte array, witch is file downloaded from specified url. It doesn't create new thread. It doesn't throw
	 * exceptions, but may return null or corrupted data.
	 * 
	 * @param context
	 *            application context
	 * @param url
	 *            http url to file
	 * @param stopper
	 *            used to stop download, may be null
	 * @param postData
	 *            may be null
	 * @param tries
	 *            connection attempts, must be at least 1
	 * @param conTimeout
	 *            millis timeout for connection, 0 means default
	 * @return byte[] containing downloaded file
	 * 
	 */
	public static byte[] download(Context context, String url, Stopper stopper, String postData, int conTimeout) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Beginning url download: " + url);
		SimpleHttpClient http = new SimpleHttpClient(context, url);
		http.setPostData(postData);
		http.setTimeout(conTimeout);
		InputStream in = null;
		byte[] data = null;
		try {
			in = http.send();
			data = readToByteArray(in, stopper);
		} catch (Exception e) {
			Log.e(LOG_TAG, "Exception when sending http request. " + e.getClass().getSimpleName());
			Log.e(LOG_TAG, e.getMessage());
			data = null;
		} finally {
			http.close();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.w(LOG_TAG, "Can't even close input stream!", e);
				}
			}
		}
		return data;
	}

	/**
	 * Copies whole InputStream to byte array
	 * 
	 * @param InputStream
	 *            to copy
	 * 
	 * @return byte[] containing copied InputStream
	 * 
	 */
	private static byte[] readToByteArray(InputStream is, Stopper stopper) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = new byte[1024];
		for (int s; (s = is.read(buffer)) != -1;) {
			if (stopper != null && stopper.isStopped()) return null;
			baos.write(buffer, 0, s);
		}
		return baos.toByteArray();
	}

	/**
	 * @param c
	 *            Context
	 * @return Current connection state. You shouldn't start download when connection is just it's off.
	 */
	public static boolean connectionAvailable(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			return false;
		}
	}

	public interface Stopper {
		public boolean isStopped();
	}
}
