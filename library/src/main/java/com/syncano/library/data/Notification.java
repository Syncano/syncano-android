package com.syncano.library.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.NotificationAction;
import com.syncano.library.parser.GsonParser;
import com.syncano.library.utils.SyncanoLog;

import java.util.Date;

public class Notification {

    public static final String FIELD_AUTHOR = "author";
    public static final String FIELD_ID = "id";
    public static final String FIELD_ROOM = "room";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_ACTION = "action";
    public static final String FIELD_PAYLOAD = "payload";
    public static final String FIELD_METADATA = "metadata";

    @SyncanoField(name = FIELD_AUTHOR, readOnly = true)
    private JsonObject author;

    @SyncanoField(name = FIELD_ID, readOnly = true)
    private int id;

    @SyncanoField(name = FIELD_CREATED_AT, readOnly = true)
    private Date createdAt;

    @SyncanoField(name = FIELD_ACTION, readOnly = true)
    private NotificationAction action;

    @SyncanoField(name = FIELD_METADATA, readOnly = true)
    private JsonObject metadata;

    @SyncanoField(name = FIELD_ROOM)
    private String room;

    @SyncanoField(name = FIELD_PAYLOAD)
    private JsonObject payload;

    public Notification() {
    }

    public Notification(JsonObject payload) {
        this(null, payload);
    }

    public Notification(String room, JsonObject payload) {
        this.room = room;
        this.payload = payload;
    }

    public JsonObject getAuthor() {
        return author;
    }

    public void setAuthor(JsonObject author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public NotificationAction getAction() {
        return action;
    }

    public void setAction(NotificationAction action) {
        this.action = action;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public void setPayload(JsonObject payload) {
        this.payload = payload;
    }

    public JsonObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonObject metadata) {
        this.metadata = metadata;
    }

    public <T> T getPayloadAs(Class<T> type) {
        if (payload == null) {
            return null;
        }
        try {
            return GsonParser.createGson(type).fromJson(payload, type);
        } catch (JsonSyntaxException e) {
            SyncanoLog.e(Notification.class.getSimpleName(), e.getClass().getSimpleName() + " Can't parse " + type.getSimpleName());
        }
        return null;
    }
}
