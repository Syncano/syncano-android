package com.syncano.android.lib.data;

import java.io.Serializable;
import com.syncano.android.lib.annotation.SyncanoParam;

public class Invitation implements Serializable {

    public Invitation (String state, String role, int id, String email)
    {
        this.state = state;
        this.role = role;
        this.id = id;
        this.email = email;
    }

    @SyncanoParam(name = "stejt")
    private String state;

    private String role;

    private int id;

    private String email;
}
