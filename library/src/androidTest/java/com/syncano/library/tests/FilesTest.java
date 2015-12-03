package com.syncano.library.tests;

import android.content.res.AssetManager;
import android.util.Log;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoClass;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FilesTest extends SyncanoApplicationTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        copyAssets();
        syncano.deleteSyncanoClass(TheObject.class).send();
        Response<SyncanoClass> resp = syncano.createSyncanoClass(TheObject.class).send();
        assertEquals(Response.HTTP_CODE_CREATED, resp.getHttpResultCode());
    }

    @Override
    protected void tearDown() throws Exception {
        syncano.deleteSyncanoClass(TheObject.class).send();
        super.tearDown();
    }

    public void testSendFile() {
        TheObject fileNull = new TheObject();
        fileNull.file = null;
        Response<TheObject> respNull = syncano.createObject(fileNull).send();
        assertEquals(Response.HTTP_CODE_CREATED, respNull.getHttpResultCode());
        assertNotNull(respNull.getData());
        assertTrue(respNull.getData().file == null);

        TheObject fileBytes = new TheObject();
        fileBytes.file = new SyncanoFile("ojojoj".getBytes());
        Response<TheObject> respBytes = syncano.createObject(fileBytes).send();
        assertEquals(Response.HTTP_CODE_CREATED, respBytes.getHttpResultCode());
        assertNotNull(respBytes.getData());
        assertNotNull(respBytes.getData().file);
        assertNotNull(respBytes.getData().file.getLink());

        TheObject fileAsset = new TheObject();
        fileAsset.file = new SyncanoFile(new File(getContext().getFilesDir(), "blue.png"));
        Response<TheObject> respAsset = syncano.createObject(fileAsset).send();
        assertEquals(Response.HTTP_CODE_CREATED, respAsset.getHttpResultCode());
        assertNotNull(respAsset.getData());
        assertNotNull(respAsset.getData().file);
        assertNotNull(respAsset.getData().file.getLink());
        assertTrue(respAsset.getData().file2 == null);

        TheObject fileDouble = new TheObject();
        fileDouble.file = new SyncanoFile(new File(getContext().getFilesDir(), "blue.png"));
        fileDouble.file2 = new SyncanoFile(new File(getContext().getFilesDir(), "red.png"));
        Response<TheObject> respDouble = syncano.createObject(fileDouble).send();
        assertEquals(Response.HTTP_CODE_CREATED, respDouble.getHttpResultCode());
        assertNotNull(respDouble.getData());
        assertNotNull(respDouble.getData().file);
        assertNotNull(respDouble.getData().file.getLink());
        assertNotNull(respDouble.getData());
        assertNotNull(respDouble.getData().file2);
        assertNotNull(respDouble.getData().file2.getLink());
        assertEquals(fileDouble.notImportantText, respDouble.getData().notImportantText);
    }

    @com.syncano.library.annotation.SyncanoClass(name = "object_with_file")
    private static class TheObject extends SyncanoObject {
        @SyncanoField(name = "text")
        String notImportantText = "lorem ipsum...";
        @SyncanoField(name = "myfile")
        SyncanoFile file;
        @SyncanoField(name = "myfilesecond")
        SyncanoFile file2;
    }

    private void copyAssets() {
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
