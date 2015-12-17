package com.syncano.library.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.syncano.library.annotation.SyncanoField;
import com.syncano.library.choice.ClassStatus;
import com.syncano.library.choice.SyncanoClassPermissions;
import com.syncano.library.utils.SyncanoClassHelper;

import java.util.Date;

public class SyncanoClass {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_SCHEMA = "schema";
    public static final String FIELD_STATUS = "status";
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_UPDATED_AT = "updated_at";
    public static final String FIELD_OBJECTS_COUNT = "objects_count";
    public static final String FIELD_REVISION = "revision";
    public static final String FIELD_GROUP = "group";
    public static final String FIELD_GROUP_PERMISSIONS = "group_permissions";
    public static final String FIELD_OTHER_PERMISSIONS = "other_permissions";
    public static final String FIELD_METADATA = "metadata";

    @SyncanoField(name = FIELD_NAME, readOnly = true, required = true)
    private String name;

    @SyncanoField(name = FIELD_DESCRIPTION)
    private String description;

    @SyncanoField(name = FIELD_SCHEMA, required = true)
    private JsonArray schema;

    @SyncanoField(name = FIELD_STATUS, readOnly = true)
    private ClassStatus status;

    @SyncanoField(name = FIELD_CREATED_AT, readOnly = true)
    private Date createdAt;

    @SyncanoField(name = FIELD_UPDATED_AT, readOnly = true)
    private Date updatedAt;

    @SyncanoField(name = FIELD_OBJECTS_COUNT, readOnly = true)
    private int objectsCount;

    @SyncanoField(name = FIELD_REVISION, readOnly = true)
    private int revision;

    @SyncanoField(name = FIELD_GROUP)
    private Integer group;

    @SyncanoField(name = FIELD_GROUP_PERMISSIONS)
    private SyncanoClassPermissions groupPermissions;

    @SyncanoField(name = FIELD_OTHER_PERMISSIONS)
    private SyncanoClassPermissions otherPermissions;

    @SyncanoField(name = FIELD_METADATA)
    private JsonElement metadata;

    public SyncanoClass() {
    }

    public SyncanoClass(String name, JsonArray schema) {
        this.name = name;
        this.schema = schema;
    }

    public SyncanoClass(Class<? extends SyncanoObject> clazz) {
        this.name = SyncanoClassHelper.getSyncanoClassName(clazz);
        this.schema = SyncanoClassHelper.getSyncanoClassSchema(clazz);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JsonArray getSchema() {
        return schema;
    }

    public void setSchema(JsonArray schema) {
        this.schema = schema;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public void setStatus(ClassStatus status) {
        this.status = status;
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

    public int getObjectsCount() {
        return objectsCount;
    }

    public void setObjectsCount(int objectsCount) {
        this.objectsCount = objectsCount;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public SyncanoClassPermissions getGroupPermissions() {
        return groupPermissions;
    }

    public void setGroupPermissions(SyncanoClassPermissions groupPermissions) {
        this.groupPermissions = groupPermissions;
    }

    public SyncanoClassPermissions getOtherPermissions() {
        return otherPermissions;
    }

    public void setOtherPermissions(SyncanoClassPermissions otherPermissions) {
        this.otherPermissions = otherPermissions;
    }

    public JsonElement getMetadata() {
        return metadata;
    }

    public void setMetadata(JsonElement metadata) {
        this.metadata = metadata;
    }
}
