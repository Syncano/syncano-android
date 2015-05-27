package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.ChannelPermissions;
import com.syncano.library.choice.ChannelType;

import java.util.Date;

public class Channel {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_GROUP = "group";
    public static final String FIELD_GROUP_PERMISSIONS = "group_permissions";
    public static final String FIELD_OTHER_PERMISSIONS = "other_permissions";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_UPDATED_AT = "updated_at";
    public static final String FIELD_CUSTOM_PUBLISH = "custom_publish";

    @SyncanoField(name = FIELD_NAME, required = true)
    private String name;

    @SyncanoField(name = FIELD_TYPE, required = true)
    private ChannelType type;

    @SyncanoField(name = FIELD_GROUP)
    private Group group;

    @SyncanoField(name = FIELD_GROUP_PERMISSIONS, required = true)
    private ChannelPermissions groupPermissions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS, required = true)
    private ChannelPermissions otherPermissions;

    @SyncanoField(name = FIELD_CREATED_AT, readOnly = true)
    private Date createdAt;

    @SyncanoField(name = FIELD_UPDATED_AT, readOnly = true)
    private Date updatedAt;

    @SyncanoField(name = FIELD_CUSTOM_PUBLISH)
    private boolean customPublish;

    public Channel() {
    }

    public Channel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChannelType getType() {
        return type;
    }

    public void setType(ChannelType type) {
        this.type = type;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public ChannelPermissions getGroupPermissions() {
        return groupPermissions;
    }

    public void setGroupPermissions(ChannelPermissions groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

    public ChannelPermissions getOtherPermissions() {
        return otherPermissions;
    }

    public void setOtherPermissions(ChannelPermissions otherPermissions) {
        this.otherPermissions = otherPermissions;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isCustomPublish() {
        return customPublish;
    }

    public void setCustomPublish(boolean customPublish) {
        this.customPublish = customPublish;
    }
}
