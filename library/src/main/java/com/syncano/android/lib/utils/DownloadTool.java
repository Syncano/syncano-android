package com.syncano.android.lib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.util.Log;

import com.syncano.android.lib.BuildConfig;
import com.syncano.android.lib.api.SyncanoException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Used to easy download files.
 */
public class DownloadTool {

	private final static String LOG_TAG = DownloadTool.class.getSimpleName();

	/**
	 * Returns a byte array, witch is file downloaded from specified url. If downloading fails, it tries again until
	 * limit reached. It doesn't create new thread. It doesn't throw exceptions, but may return null or corrupted data.
	 *
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
	public static byte[] download(String requestMethod, String apiKey, String url, Stopper stopper, String postData, int tries,
			int conTimeout) throws SyncanoException {
		int tryNum = 0;
		byte[] data = null;
		while (data == null && tryNum < tries) {
			if (stopper != null && stopper.isStopped()) return null;
			data = download(requestMethod, apiKey, url, stopper, postData, conTimeout);
			tryNum++;
		}
		return data;
	}

	/**
	 * Returns a byte array, witch is file downloaded from specified url. It doesn't create new thread. It doesn't throw
	 * exceptions, but may return null or corrupted data.

	 * @param url
	 *            http url to file
	 * @param stopper
	 *            used to stop download, may be null
	 * @param postData
	 *            may be null
	 * @param conTimeout
	 *            millis timeout for connection, 0 means default
	 * @return byte[] containing downloaded file
	 * 
	 */
	public static byte[] download(String requestMethod, String apiKey, String url, Stopper stopper, String postData, int conTimeout) throws SyncanoException {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Beginning url download: " + url);
		SimpleHttpClient http = new SimpleHttpClient(url);
		http.setPostData(postData);
		http.setTimeout(conTimeout);
		InputStream in = null;
		byte[] data = null;
		try {
			in = http.send(apiKey, requestMethod);
			data = readToByteArray(in, stopper);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Exception when sending http request. " + e.getClass().getSimpleName());
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
	 * @param is InputStream to copy
	 * 
	 * @return byte[] containing copied InputStream
	 * 
	 */
	private static byte[] readToByteArray(InputStream is, Stopper stopper) throws IOException {
		if (is == null)
            return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = new byte[1024];
		for (int s; (s = is.read(buffer)) != -1;) {
			if (stopper != null && stopper.isStopped()) return null;
			baos.write(buffer, 0, s);
		}
		return baos.toByteArray();
	}

	public interface Stopper {
		public boolean isStopped();
	}

	/**
	 * Method handling image downloading.
	 * Should not be called on UI Thread.
	 * @return Bitmap from given URL
	 */
	public static Bitmap downloadImage(URL url) {
		if (BuildConfig.DEBUG) Log.d(LOG_TAG, "Beginning img download: " + url);
		
		Bitmap result = null;
		InputStream is = null;
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            is = connection.getInputStream();
            result = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	connection.disconnect();
        	if (is != null) {
        		try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
		
		return result;
	}
}
