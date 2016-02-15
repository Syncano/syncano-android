package com.syncano.library.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.FieldType;
import com.syncano.library.data.SyncanoFile;
import com.syncano.library.data.SyncanoObject;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.utils.SyncanoClassHelper;
import com.syncano.library.utils.SyncanoLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public abstract class SendRequest<T> extends ResultRequest<T> {
    private final static String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
    private final static String twoHyphens = "--";
    private final static String lineEnd = "\r\n";
    private Object data;
    private boolean updateGivenData = false;

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
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return synObjEntity;
    }

    protected InputStreamEntity prepareSyncanoObjectEntity() throws IOException, IllegalAccessException {
        if (data == null) {
            return null;
        }
        ArrayList<InputStream> inputStreams = new ArrayList<>();
        inputStreams.add(getStringFieldsInputStream());
        inputStreams.addAll(getFileInputStreams());
        inputStreams.add(getEndInputStream());
        SequenceInputStream sequenceInputStream = new SequenceInputStream(Collections.enumeration(inputStreams));
        // -1 means unknown;
        return new InputStreamEntity(sequenceInputStream, -1);
    }

    private ArrayList<InputStream> getFileInputStreams() throws IllegalAccessException, IOException {
        ArrayList<InputStream> streamsList = new ArrayList<>();
        for (Field field : data.getClass().getDeclaredFields()) {
            SyncanoField fieldAnnotation = field.getAnnotation(SyncanoField.class);
            if (fieldAnnotation == null) {
                continue;
            }
            FieldType type = SyncanoClassHelper.findType(field, fieldAnnotation);
            if (type == null || !FieldType.FILE.equals(type)) {
                continue;
            }

            field.setAccessible(true);
            SyncanoFile synFile = (SyncanoFile) field.get(data);
            if (synFile == null) {
                continue;
            }

            String fieldName = SyncanoClassHelper.getFieldName(field);
            File file = synFile.getFile();
            byte[] byteData = synFile.getData();
            String fileName;
            if (byteData != null) {
                fileName = fieldName;
            } else if (file != null) {
                fileName = file.getName();
            } else {
                continue;
            }

            streamsList.add(getItemStartInputStream(fieldName, fileName));
            if (byteData != null) {
                streamsList.add(new ByteArrayInputStream(byteData));
            } else {
                streamsList.add(new FileInputStream(file));
            }
            streamsList.add(new ByteArrayInputStream((lineEnd).getBytes("UTF-8")));
        }
        return streamsList;
    }

    private InputStream getStringFieldsInputStream() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);
        JsonObject json = gson.toJsonTree(data).getAsJsonObject();
        ((SyncanoObject) data).getIncrementBuilder().build(json);
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            os.writeBytes(twoHyphens + boundary + lineEnd);
            os.writeBytes("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + lineEnd);
            os.writeBytes("Content-Type: text/plain" + lineEnd);
            os.writeBytes(lineEnd);
            os.writeBytes(getJsonElementAsString(entry.getValue()));
            os.writeBytes(lineEnd);
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private String getJsonElementAsString(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive())
            return jsonElement.getAsString();
        else
            return jsonElement.toString();
    }

    private InputStream getItemStartInputStream(String name, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(baos);

        os.writeBytes(twoHyphens + boundary + lineEnd);
        os.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + filename + "\"" + lineEnd);
        os.writeBytes("Content-Type: application/octet-stream" + lineEnd);
        os.writeBytes(lineEnd);

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private InputStream getEndInputStream() throws UnsupportedEncodingException {
        return new ByteArrayInputStream((twoHyphens + boundary + twoHyphens + lineEnd).getBytes("UTF-8"));
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

    public JsonElement prepareJsonParams() {
        if (data == null) {
            return null;
        }
        return gson.toJsonTree(data);
    }

    @Override
    public String getContentType() {
        if (data == null || !(data instanceof SyncanoObject)) {
            return "application/json";
        }
        return "multipart/form-data; boundary=" + boundary;
    }

    @Override
    public T parseResult(Response<T> response, String json) {
        if (updateGivenData) {
            if (data.getClass().isAssignableFrom(resultType)) {
                return GsonParser.createGson(data).fromJson(json, resultType);
            } else {
                SyncanoLog.w(SendRequest.class.getSimpleName(),
                        "Can't update object " + data.getClass().getSimpleName() + " from " + resultType.getSimpleName());
            }
        }
        return super.parseResult(response, json);
    }

    public boolean isSetUpdateGivenObject() {
        return updateGivenData;
    }

    public void updateGivenObject(boolean updateGivenData) {
        this.updateGivenData = updateGivenData;
    }
}
