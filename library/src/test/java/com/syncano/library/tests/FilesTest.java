package com.syncano.library.tests;

import com.syncano.library.SyncanoApplicationTestCase;
import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.Response;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.utils.FileDownloader;
import com.syncano.library.utils.SyncanoLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FilesTest extends SyncanoApplicationTestCase {
    @Before
    public void setUp() throws Exception {
        super.setUp();
        createClass(TheObject.class);
    }

    @After
    public void tearDown() throws Exception {
        removeClass(TheObject.class);
        super.tearDown();
    }

    @Test
    public void testSendFile() {
        TheObject fileNull = new TheObject();
        fileNull.file = null;
        Response<TheObject> respNull = syncano.createObject(fileNull).send();
        assertTrue(respNull.isSuccess());
        assertNotNull(respNull.getData());
        assertTrue(respNull.getData().file == null);

        TheObject fileBytes = new TheObject();
        fileBytes.file = new SyncanoFile("ojojoj".getBytes());
        Response<TheObject> respBytes = syncano.createObject(fileBytes).send();
        assertTrue(respBytes.isSuccess());
        assertNotNull(respBytes.getData());
        assertNotNull(respBytes.getData().file);
        assertNotNull(respBytes.getData().file.getLink());

        TheObject fileAsset = new TheObject();
        fileAsset.file = new SyncanoFile(new File(getAssetsDir(), "blue.png"));
        Response<TheObject> respAsset = syncano.createObject(fileAsset).send();
        assertTrue(respAsset.isSuccess());
        assertNotNull(respAsset.getData());
        assertNotNull(respAsset.getData().file);
        assertNotNull(respAsset.getData().file.getLink());
        assertTrue(respAsset.getData().file2 == null);

        TheObject fileDouble = new TheObject();
        fileDouble.file = new SyncanoFile(new File(getAssetsDir(), "blue.png"));
        fileDouble.file2 = new SyncanoFile(new File(getAssetsDir(), "red.png"));
        Response<TheObject> respDouble = syncano.createObject(fileDouble).send();
        assertTrue(respDouble.isSuccess());
        assertNotNull(respDouble.getData());
        assertNotNull(respDouble.getData().file);
        assertNotNull(respDouble.getData().file.getLink());
        assertNotNull(respDouble.getData());
        assertNotNull(respDouble.getData().file2);
        assertNotNull(respDouble.getData().file2.getLink());
        assertEquals(fileDouble.notImportantText, respDouble.getData().notImportantText);
    }

    @Test
    public void testDownloadFile() throws InterruptedException {
        // send file
        TheObject obj = new TheObject();
        obj.file = new SyncanoFile(new File(getAssetsDir(), "chopin.mp3"));
        Response<TheObject> resp = obj.save();
        assertTrue(resp.isSuccess());
        assertNotNull(obj.file.getLink());

        // download as byte array
        final CountDownLatch l1 = new CountDownLatch(1);
        obj.file.fetch(new FileDownloader.FileDownloadCallback() {
            @Override
            public void progress(float fraction) {
                SyncanoLog.d(FilesTest.class.getSimpleName(), "Downloading " + (fraction * 100) + "%");
            }

            @Override
            public void finished(SyncanoFile file) {
                assertTrue(file.getData() != null && file.getData().length > 0);
                l1.countDown();
            }

            @Override
            public void error(String message) {
                SyncanoLog.d(FilesTest.class.getSimpleName(), message);
                fail();
                l1.countDown();
            }
        });
        l1.await();

        // download to file
        final CountDownLatch l2 = new CountDownLatch(1);
        File path = new File(getBuildsDir(), "testfile.mp3");
        obj.file.fetch(path, new FileDownloader.FileDownloadCallback() {
            @Override
            public void progress(float fraction) {
                SyncanoLog.d(FilesTest.class.getSimpleName(), "Downloading " + (fraction * 100) + "%");
            }

            @Override
            public void finished(SyncanoFile file) {
                assertNotNull(file.getFile());
                l2.countDown();
            }

            @Override
            public void error(String message) {
                SyncanoLog.d(FilesTest.class.getSimpleName(), message);
                fail();
                l2.countDown();
            }
        });
        l2.await();
    }

    @SyncanoClass(name = "object_with_file")
    private static class TheObject extends SyncanoObject {
        @SyncanoField(name = "text")
        String notImportantText = "lorem ipsum...";
        @SyncanoField(name = "myfile")
        SyncanoFile file;
        @SyncanoField(name = "myfilesecond")
        SyncanoFile file2;
    }
}
