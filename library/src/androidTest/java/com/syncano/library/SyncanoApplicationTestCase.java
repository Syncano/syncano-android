package com.syncano.library;

import android.app.Application;
import android.content.res.AssetManager;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class SyncanoApplicationTestCase extends ApplicationTestCase<Application> {

    protected Syncano syncano;

    public SyncanoApplicationTestCase() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Syncano.init(BuildConfig.STAGING_SERVER_URL, BuildConfig.API_KEY, BuildConfig.INSTANCE_NAME);
        syncano = Syncano.getInstance();
    }

    public void createClass(Class<? extends SyncanoObject> clazz) {
        removeClass(clazz);
        Response resp = syncano.createSyncanoClass(clazz).send();
        assertTrue(resp.isSuccess());
    }

    public void removeClass(Class<? extends SyncanoObject> clazz) {
        Response<SyncanoClass> resp = syncano.deleteSyncanoClass(clazz).send();
        assertTrue(resp.isSuccess());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void copyAssets() {
        AssetManager assetManager = getContext().getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getContext().getFilesDir(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch (IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // nothing
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // nothing
                    }
                }
            }
        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}