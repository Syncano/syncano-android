package com.syncano.library.data;

import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.ChannelPermissions;

public class Channel {

    public static final String FIELD_GROUP = "group";
    public static final String FIELD_GROUP_PERMISSIONS = "group_permissions";
    public static final String FIELD_OTHER_PERMISSIONS = "other_permissions";

    @SyncanoField(name = FIELD_GROUP)
    private Group group;

    @SyncanoField(name = FIELD_GROUP_PERMISSIONS, required = true)
    private ChannelPermissions groupPermissions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS, required = true)
    private ChannelPermissions otherPermissions;

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
}
