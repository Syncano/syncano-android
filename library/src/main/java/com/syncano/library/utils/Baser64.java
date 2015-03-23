package com.syncano.library.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Baser64 {
	private final static String LOG_TAG = Baser64.class.getSimpleName();
	/** Max size of image dimension(width or height) */
	private final static int DEFAULT_MAX_SIZE = 1000;

	/**
	 * Method to load image in jpg format and return it in Base64 format
	 * 
	 * @param path
	 *            path to image file
	 * @return Base64 coded jpg image string
	 */
	public static String getBaseImageJpg(String path) {
		return getBaseImageJpg(path, DEFAULT_MAX_SIZE, DEFAULT_MAX_SIZE);
	}

	/**
	 * Method to load image in jpg format and return it in Base64 format
	 * 
	 * @param path
	 *            path to image file
	 * @param maxWidth
	 *            max width of file that we want to convert to Base64
	 * @param maxHeight
	 *            max height of file that we want to convert to Base64
	 * @return Base64 coded jpg image string
	 */
	public static String getBaseImageJpg(String path, int maxWidth, int maxHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opts);
		// resizing bitmap
		BitmapFactory.Options options = new BitmapFactory.Options();
		float scale = Math.max((float) opts.outWidth / (float) maxWidth, (float) opts.outHeight / (float) maxHeight);
		if (scale > 1f) {
			options.inSampleSize = ((int) scale) + 1;
		}
		Bitmap bitmap = decodeBitmap(path, options);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		String baseImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
		bitmap.recycle();
		return baseImage;
	}

	/**
	 * Method to decode bitmap from file
	 * 
	 * @param path
	 *            path to image file
	 * @param opts
	 *            bitmap options for new bitmap, can be null
	 * @return decoded bitmap with specified options applied
	 */
	private static Bitmap decodeBitmap(String path, BitmapFactory.Options opts) {
		try {
			Matrix matrix = null;
			try {
				ExifInterface exifReader = new ExifInterface(path);
				int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
				switch (orientation) {
					case ExifInterface.ORIENTATION_ROTATE_90:
						matrix = new Matrix();
						matrix.postRotate(90);
						break;
					case ExifInterface.ORIENTATION_ROTATE_180:
						matrix = new Matrix();
						matrix.postRotate(180);
						break;
					case ExifInterface.ORIENTATION_ROTATE_270:
						matrix = new Matrix();
						matrix.postRotate(270);
						break;
				}
			} catch (IOException e) {
				Log.e(LOG_TAG, "IO exception: " + e.toString());
			}

			Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
			if (matrix != null) {
				Bitmap bitmapRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
						true);
				bitmap.recycle();
				return bitmapRotated;
			}
			return bitmap;
		} catch (OutOfMemoryError e) {
			opts.inSampleSize++;
			return decodeBitmap(path, opts);
		}
	}

	/**
	 * Method to get real path to file/media from specified URI
	 * 
	 * @param c
	 *            context needed to get content resolver
	 * @param contentURI
	 *            URI to file for which we need path
	 * @return path to file specified by contentURI
	 */
	public static String getRealPathFromURI(Context c, Uri contentURI) {
		Cursor cursor = c.getContentResolver().query(contentURI, null, null, null, null);
		if (cursor == null)
			return null;
		else {
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		}
	}
}
