package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.Permissions;

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
    private Permissions groupPermisions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS, required = true)
    private Permissions otherPermissions;

    @SyncanoField(name = FIELD_CHANNEL, readOnly = true)
    private Channel channel;

    @SyncanoField(name = FIELD_CHANNEL_ROOM, readOnly = true)
    private String channelRoom;

}
