package com.syncano.android.lib.data;

import com.syncano.android.lib.annotation.SyncanoField;
import com.syncano.android.lib.choice.RuntimeName;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class CodeBox {

    @SyncanoField(name = "id", readOnly = true)
    private int id;

    @SyncanoField(name = "created_at", readOnly = true)
    private Date createdAt;

    @SyncanoField(name = "updated_at", readOnly = true)
    private Date updatedAt;

    @SyncanoField(name = "name")
    private String name;

    @SyncanoField(name = "description")
    private String description;

    @SyncanoField(name = "source")
    private String source;

    @SyncanoField(name = "runtime_name")
    private RuntimeName runtimeName;

    @SyncanoField(name = "config")
    private JSONObject config;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public RuntimeName getRuntimeName() {
        return runtimeName;
    }

    public void setRuntimeName(RuntimeName runtimeName) {
        this.runtimeName = runtimeName;
    }

    public JSONObject getConfig() {
        return config;
    }

    public void setConfig(JSONObject config) {
        this.config = config;
    }
}
