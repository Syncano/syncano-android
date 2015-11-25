package com.syncano.library.data;

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

    public String getLink() {
        return link;
    }

    public File getFile() {
        return file;
    }
}
