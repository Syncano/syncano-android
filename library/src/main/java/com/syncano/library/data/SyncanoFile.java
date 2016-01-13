package com.syncano.library.data;

import com.syncano.library.utils.FileDownloader;

import java.io.File;

public class SyncanoFile {
    private File file;
    private String link;
    private byte[] data;

    public SyncanoFile(byte[] data) {
        this.data = data;
    }

    public SyncanoFile(String link) {
        this.link = link;
    }

    public SyncanoFile(File file) {
        this.file = file;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getLink() {
        return link;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Downloads this file asynchronously. This object should have link field set. Result will be available
     * in a byte[] array. Use getData() to read it.
     */
    public void fetch(FileDownloader.FileDownloadCallback callback) {
        FileDownloader.download(this, callback);
    }

    /**
     * Downloads this file asynchronously. This object should have link field set. Result will be
     * written to given path.
     */
    public void fetch(File path, FileDownloader.FileDownloadCallback callback) {
        FileDownloader.download(this, path, callback);
    }
}
