package com.syncano.library.utils;


import com.syncano.library.PlatformType;
import com.syncano.library.data.SyncanoFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDownloader {
    private static final ExecutorService requestExecutor = Executors.newSingleThreadExecutor();
    private static final int PROGRESS_REFRESH = 200;

    /**
     * Downloads a file asynchronously from internet.
     *
     * @param url      Where to get a file from.
     * @param callback Callback. Result will be passed as a byte[]. Use getData() on a result SyncanoFile to get downloaded data.
     */
    public static void download(String url, FileDownloadCallback callback) {
        download(new SyncanoFile(url), null, true, callback);
    }

    /**
     * Downloads a file asynchronously from internet.
     *
     * @param url      Where to get a file from.
     * @param path     Path where downloaded file should be written. May be null. Then it will not be written anywhere.
     *                 Use getFile() on a result SyncanoFile to get it.
     * @param callback Callback.
     */
    public static void download(String url, File path, FileDownloadCallback callback) {
        download(new SyncanoFile(url), path, false, callback);
    }

    /**
     * Downloads a file asynchronously from internet.
     *
     * @param url      Where to get a file from.
     * @param path     Path where downloaded file should be written. May be null. Then it will not be written anywhere.
     *                 Use getFile() on a result object to get it.
     * @param getBytes Should the result contain bytes array of downloaded file. Use getData() on a result SyncanoFile to get it.
     * @param callback Callback.
     */
    public static void download(String url, File path, boolean getBytes, FileDownloadCallback callback) {
        download(new SyncanoFile(url), path, getBytes, callback);
    }

    /**
     * Downloads a file asynchronously from internet.
     *
     * @param file     File that should be downloaded. It's getLink() method will be used to get url.
     * @param callback Callback. Result will be passed as a byte[]. Use getData() on a result SyncanoFile to get downloaded data.
     */
    public static void download(SyncanoFile file, FileDownloadCallback callback) {
        download(file, null, true, callback);
    }

    /**
     * Downloads a file asynchronously from internet.
     *
     * @param file     File that should be downloaded. It's getLink() method will be used to get url.
     * @param path     Path where downloaded file should be written.
     * @param callback Callback. Result will be written to a file. Use getFile() on a result SyncanoFile to get it.
     */
    public static void download(SyncanoFile file, File path, FileDownloadCallback callback) {
        download(file, path, false, callback);
    }

    /**
     * Downloads a file asynchronously from internet.
     *
     * @param file     File that should be downloaded. It's getLink() method will be used to get url.
     * @param path     Path where downloaded file should be written. May be null. Then it will not be written anywhere.
     *                 Use getFile() on a result SyncanoFile to get it.
     * @param getBytes Should the result contain bytes array of downloaded file. Use getData() on a result SyncanoFile to get it.
     * @param callback Callback.
     */
    public static void download(final SyncanoFile file, final File path, final boolean getBytes, final FileDownloadCallback callback) {
        requestExecutor.execute(new Runnable() {
            @Override
            public void run() {
                doDownload(file, path, getBytes, callback);
            }
        });
    }

    private static void doDownload(SyncanoFile file, File path, boolean getBytes, FileDownloadCallback callback) {
        InputStream is = null;

        try {
            URL parsedUrl = new URL(file.getLink());
            HttpURLConnection conn = (HttpURLConnection) parsedUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            if (response == 200) {
                is = conn.getInputStream();
                if (is == null) {
                    callbackError(callback, "Can't read from null input stream");
                } else {
                    readIt(conn, is, file, path, getBytes, callback);
                }
            } else {
                callbackError(callback, "Http response code " + response + " " + conn.getResponseMessage());
            }
        } catch (Exception e) {
            callbackError(callback, e.getClass().getSimpleName() + " " + e.getMessage());
        } finally {
            // closing input stream
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    SyncanoLog.d(FileDownloader.class.getSimpleName(), e.getMessage());
                }
            }
        }
    }

    /**
     * Helps getting file name from url.
     */
    public static String getFileName(String url) {
        try {
            URL parsedUrl = new URL(url);
            String fileName = parsedUrl.getFile();
            int slashIndex = fileName.lastIndexOf('/');
            if (slashIndex > -1) {
                fileName = fileName.substring(slashIndex + 1);
            }
            return fileName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Helps getting file name from url.
     */
    public static String getFileName(SyncanoFile file) {
        return getFileName(file.getLink());
    }

    private static void readIt(HttpURLConnection conn, InputStream is, SyncanoFile file, File path, boolean getBytes, FileDownloadCallback callback) throws IOException {
        float contentLength = conn.getContentLength();
        float downloadedBytes = 0;

        FileOutputStream fos = null;
        if (path != null) {
            path.getParentFile().mkdirs();
            fos = new FileOutputStream(path, false);
        }
        ByteArrayOutputStream baos = null;
        if (getBytes) {
            baos = new ByteArrayOutputStream();
        }

        long lastProgressRefresh = 0;
        byte buffer[] = new byte[1024];
        for (int s; (s = is.read(buffer)) != -1; ) {
            downloadedBytes += s;
            // write bytes to array
            if (baos != null) {
                baos.write(buffer, 0, s);
            }
            // write data to file
            if (fos != null) {
                fos.write(buffer, 0, s);
            }
            // refresh progress
            if (contentLength > 0 && System.currentTimeMillis() - lastProgressRefresh > PROGRESS_REFRESH) {
                callbackProgress(callback, downloadedBytes / contentLength);
                lastProgressRefresh = System.currentTimeMillis();
            }
        }

        if (baos != null) {
            baos.close();
            file.setData(baos.toByteArray());
        }
        if (fos != null) {
            fos.close();
            file.setFile(path);
        }

        callbackSuccess(callback, file);
    }

    private static void callbackError(final FileDownloadCallback callback, final String message) {
        PlatformType.get().runOnCallbackThread(new Runnable() {
            @Override
            public void run() {
                callback.error(message);
            }
        });
    }

    private static void callbackProgress(final FileDownloadCallback callback, final float progress) {
        PlatformType.get().runOnCallbackThread(new Runnable() {
            @Override
            public void run() {
                callback.progress(progress);
            }
        });
    }

    private static void callbackSuccess(final FileDownloadCallback callback, final SyncanoFile result) {
        PlatformType.get().runOnCallbackThread(new Runnable() {
            @Override
            public void run() {
                callback.finished(result);
            }
        });
    }

    public interface FileDownloadCallback {
        /**
         * When downloader has information about progress, then this method is called periodically
         * to inform about progress.
         *
         * @param fraction minimum 0, maximum 1
         */
        void progress(float fraction);

        /**
         * Called when download is finished.
         *
         * @param file Has File or/and Data fields set depending on previously setting of downloader.
         *             Use getFile(), getData() to get downloaded data.
         */
        void finished(SyncanoFile file);

        /**
         * Called when something was wrong. finished() will not be called.
         */
        void error(String message);
    }
}
