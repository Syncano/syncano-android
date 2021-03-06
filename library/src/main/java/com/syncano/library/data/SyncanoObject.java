package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.DataObjectPermissions;
import com.syncano.library.simple.ObjectRequestBuilder;

public abstract class SyncanoObject extends ObjectRequestBuilder {

    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_EXPECTED_REVISION = "expected_revision";
    public static final String FIELD_OWNER = "owner";
    public static final String FIELD_OWNER_PERMISSIONS = "owner_permissions";
    public static final String FIELD_GROUP = "group";
    public static final String FIELD_GROUP_PERMISSIONS = "group_permissions";
    public static final String FIELD_OTHER_PERMISSIONS = "other_permissions";
    public static final String FIELD_CHANNEL = "channel";
    public static final String FIELD_CHANNEL_ROOM = "channel_room";
    public static final String FIELD_LOCAL_ID = "id_local";

    @SyncanoField(name = FIELD_OWNER, inSchema = false)
    private Integer owner;

    @SyncanoField(name = FIELD_OWNER_PERMISSIONS, inSchema = false)
    private DataObjectPermissions ownerPermissions;

    @SyncanoField(name = FIELD_GROUP, offlineName = "_group", inSchema = false)
    private Integer group;

    @SyncanoField(name = FIELD_GROUP_PERMISSIONS, inSchema = false)
    private DataObjectPermissions groupPermissions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS, inSchema = false)
    private DataObjectPermissions otherPermissions;

    @SyncanoField(name = FIELD_CHANNEL, inSchema = false)
    private String channel;

    @SyncanoField(name = FIELD_CHANNEL_ROOM, inSchema = false)
    private String channelRoom;

    @SyncanoField(name = FIELD_REVISION, inSchema = false)
    private Integer revision;

    @SyncanoField(name = FIELD_EXPECTED_REVISION, inSchema = false)
    private Integer expectedRevision;

    @SyncanoField(name = FIELD_LOCAL_ID, onlyLocal = true)
    private Long localId;

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

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }
}
