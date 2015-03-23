package com.syncano.library.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.syncano.library.lib.BuildConfig;

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
