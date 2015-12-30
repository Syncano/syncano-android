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

    public static void download(String url, FileDownloadCallback callback) {
        download(new SyncanoFile(url), null, true, callback);
    }

    public static void download(String url, File path, FileDownloadCallback callback) {
        download(new SyncanoFile(url), path, false, callback);
    }

    public static void download(SyncanoFile file, FileDownloadCallback callback) {
        download(file, null, true, callback);
    }

    public static void download(SyncanoFile file, File path, FileDownloadCallback callback) {
        download(file, path, false, callback);
    }

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
        void progress(float fraction);

        void finished(SyncanoFile file);

        void error(String message);
    }
}
