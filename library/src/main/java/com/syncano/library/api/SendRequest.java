package com.syncano.library.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.data.SyncanoObject;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract class SendRequest<T> extends ResultRequest<T> {
    private Object data;
    private String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";

    protected SendRequest(Class<T> resultType, String url, Syncano syncano, Object data) {
        super(resultType, url, syncano);
        this.data = data;
    }

    @Override
    public HttpEntity prepareParams() {
        if (data == null) {
            return null;
        }

        if (!(data instanceof SyncanoObject)) {
            return prepareStringEntity();
        }

        InputStreamEntity synObjEntity = null;
        try {
            synObjEntity = prepareSyncanoObjectEntity();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return synObjEntity;
    }

    protected InputStreamEntity prepareSyncanoObjectEntity() throws IOException {
        if (data == null) {
            return null;
        }

        JsonObject json = gson.toJsonTree(data).getAsJsonObject();

        String twoHyphens = "--";
        String lineEnd = "\r\n";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            os.writeBytes(twoHyphens + boundary + lineEnd);
            os.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd);
            os.writeBytes("Content-Type: text/plain" + lineEnd);
            os.writeBytes(lineEnd);
            os.writeBytes(entry.getValue().getAsString());
            os.writeBytes(lineEnd);
        }
        os.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        byte[] arr = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(arr);
        InputStreamEntity ise = new InputStreamEntity(bais, -1); // -1 means unknown
        return ise;
    }

    protected StringEntity prepareStringEntity() {
        String text = gson.toJson(data);
        try {
            return new StringEntity(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getContentType() {
        if (data == null || !(data instanceof SyncanoObject)) {
            return "application/json";
        }
        return "multipart/form-data; boundary=" + boundary;
    }
}
