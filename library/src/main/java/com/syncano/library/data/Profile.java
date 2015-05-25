package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoClass;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.Permissions;

@SyncanoClass(name = "user_profile")
public class Profile extends SyncanoObject {

    public static final String FIELD_OWNER = "owner";
    public static final String FIELD_OWNER_PERMISSIONS = "owner_permissions";
    public static final String FIELD_GROUP = "group";
    public static final String FIELD_GROUP_PERMISSIONS = "group_permissions";
    public static final String FIELD_OTHER_PERMISSIONS = "other_permissions";
    public static final String FIELD_CHANNEL= "channel";
    public static final String FIELD_CHANNEL_ROOM = "channel_room";

    @SyncanoField(name = FIELD_OWNER)
    private int owner;

    @SyncanoField(name = FIELD_OWNER_PERMISSIONS, required = true)
    private Permissions ownerPermissions;

    @SyncanoField(name = FIELD_GROUP)
    private Group group;

    @SyncanoField(name = FIELD_GROUP_PERMISSIONS, required = true)
    private Permissions groupPermissions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS, required = true)
    private Permissions otherPermissions;

    @SyncanoField(name = FIELD_CHANNEL, readOnly = true)
    private Channel channel;

    @SyncanoField(name = FIELD_CHANNEL_ROOM, readOnly = true)
    private String channelRoom;

    public Profile() {
    }

    public String getChannelRoom() {
        return channelRoom;
    }

    public void setChannelRoom(String channelRoom) {
        this.channelRoom = channelRoom;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public Permissions getOwnerPermissions() {
        return ownerPermissions;
    }

    public void setOwnerPermissions(Permissions ownerPermissions) {
        this.ownerPermissions = ownerPermissions;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Permissions getGroupPermisions() {
        return groupPermissions;
    }

    public void setGroupPermisions(Permissions groupPermisions) {
        this.groupPermissions = groupPermisions;
    }

    public Permissions getOtherPermissions() {
        return otherPermissions;
    }

    public void setOtherPermissions(Permissions otherPermissions) {
        this.otherPermissions = otherPermissions;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
