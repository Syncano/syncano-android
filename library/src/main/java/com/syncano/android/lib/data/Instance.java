package com.syncano.android.lib.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.choice.Role;

import java.util.Date;

public class Instance {

    @Expose
    private String name;

    @Expose
    @SerializedName(value = "created_at")
    private Date createdAt;

    @Expose
    @SerializedName(value = "updated_at")
    private Date updatedAt;

    @Expose
    private Role role;

    @Expose
    private AdminFull owner;

    @Expose
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public AdminFull getOwner() {
        return owner;
    }

    public void setOwner(AdminFull owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
