package com.syncano.android.lib.data;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.choice.Role;
import com.syncano.android.lib.choice.State;

import java.io.Serializable;

public class AdminInvitation implements Serializable {

    @Expose
    private State state;

    @Expose
    private Role role;

    @Expose
    private int id;

    @Expose
    private String email;

    public AdminInvitation() {
    }

    public AdminInvitation(State state, Role role, int id, String email)
    {
        this.state = state;
        this.role = role;
        this.id = id;
        this.email = email;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
