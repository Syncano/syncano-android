package com.syncano.library.data;

import com.syncano.library.Syncano;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.api.IncrementBuilder;
import com.syncano.library.api.HttpRequest;
import com.syncano.library.api.Response;
import com.syncano.library.callbacks.SyncanoCallback;
import com.syncano.library.choice.DataObjectPermissions;

import java.util.HashSet;

public abstract class SyncanoObject extends Entity {

    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_EXPECTED_REVISION = "expected_revision";
    public static final String FIELD_OWNER = "owner";
    public static final String FIELD_OWNER_PERMISSIONS = "owner_permissions";
    public static final String FIELD_GROUP = "group";
    public static final String FIELD_GROUP_PERMISSIONS = "group_permissions";
    public static final String FIELD_OTHER_PERMISSIONS = "other_permissions";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_CHANNEL_ROOM = "channel_room";

    @SyncanoField(name = FIELD_OWNER)
    private Integer owner;

    @SyncanoField(name = FIELD_OWNER_PERMISSIONS)
    private DataObjectPermissions ownerPermissions;

    @SyncanoField(name = FIELD_GROUP, offlineName = "_group")
    private Integer group;

    @SyncanoField(name = FIELD_GROUP_PERMISSIONS)
    private DataObjectPermissions groupPermissions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS)
    private DataObjectPermissions otherPermissions;

    @SyncanoField(name = FIELD_CHANNEL)
    private String channel;

    @SyncanoField(name = FIELD_CHANNEL_ROOM)
    private String channelRoom;

    @SyncanoField(name = FIELD_REVISION)
    private Integer revision;

    @SyncanoField(name = FIELD_EXPECTED_REVISION)
    private Integer expectedRevision;

    private Syncano syncano;
    private IncrementBuilder incrementBuilder = new IncrementBuilder();
    private HashSet<String> fieldsToClear = new HashSet<>();

    public String getChannelRoom() {
        return channelRoom;
    }

    public void setChannelRoom(String channelRoom) {
        this.channelRoom = channelRoom;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public DataObjectPermissions getOwnerPermissions() {
        return ownerPermissions;
    }

    public void setOwnerPermissions(DataObjectPermissions ownerPermissions) {
        this.ownerPermissions = ownerPermissions;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public DataObjectPermissions getGroupPermisions() {
        return groupPermissions;
    }

    public void setGroupPermisions(DataObjectPermissions groupPermisions) {
        this.groupPermissions = groupPermisions;
    }

    public DataObjectPermissions getOtherPermissions() {
        return otherPermissions;
    }

    public void setOtherPermissions(DataObjectPermissions otherPermissions) {
        this.otherPermissions = otherPermissions;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public int getExpectedRevision() {
        return expectedRevision;
    }

    public void setExpectedRevision(int expectedRevision) {
        this.expectedRevision = expectedRevision;
    }

    public void clearField(String fieldName) {
        fieldsToClear.add(fieldName);
    }

    public void removeFromClearList(String fieldName) {
        fieldsToClear.remove(fieldName);
    }

    public boolean isOnClearList(String fieldName) {
        return fieldsToClear.contains(fieldName);
    }

    public IncrementBuilder getIncrementBuilder() {
        return incrementBuilder;
    }

    public Syncano getSyncano() {
        if (syncano == null) {
            return Syncano.getInstance();
        }
        return syncano;
    }

    public <T extends SyncanoObject> T on(Syncano syncano) {
        this.syncano = syncano;
        return (T) this;
    }

    public <T extends SyncanoObject> Response<T> save() {
        if (getId() == null) {
            return getSyncano().createObject((T) this, true).send();
        }
        return getSyncano().updateObject((T) this, true).send();
    }

    public <T extends SyncanoObject> void save(SyncanoCallback<T> callback) {
        if (getId() == null) {
            getSyncano().createObject((T) this, false).sendAsync(callback);
        } else {
            getSyncano().updateObject((T) this, false).sendAsync(callback);
        }
    }

    public <T extends SyncanoObject> Response<T> delete() {
        return getSyncano().deleteObject((T) this).send();
    }

    public <T extends SyncanoObject> void delete(SyncanoCallback<T> callback) {
        getSyncano().deleteObject((T) this).sendAsync(callback);
    }

    public <T extends SyncanoObject> Response<T> fetch() {
        HttpRequest<T> req = (HttpRequest<T>) getSyncano().getObject(this);
        return req.send();
    }

    public <T extends SyncanoObject> void fetch(SyncanoCallback<T> callback) {
        HttpRequest<T> req = (HttpRequest<T>) getSyncano().getObject(this);
        req.sendAsync(callback);
    }

    public <T extends SyncanoObject> T increment(String fieldName, int value) {
        incrementBuilder.increment(fieldName, value);
        return (T) this;
    }

    public <T extends SyncanoObject> T decrement(String fieldName, int value) {
        incrementBuilder.decrement(fieldName, value);
        return (T) this;
    }
}
