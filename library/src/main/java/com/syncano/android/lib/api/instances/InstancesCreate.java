package com.syncano.android.lib.api.instances;

import com.syncano.android.lib.annotation.SyncanoParam;
import com.syncano.android.lib.api.BaseApiMethod;
import com.syncano.android.lib.data.Instance;

import java.lang.reflect.Type;

public class InstancesCreate extends BaseApiMethod<Instance> {

    @SyncanoParam(required = true)
    public String name;

    @SyncanoParam
    public String description;

    public InstancesCreate() {
    }

    public InstancesCreate(String name) {
        this.name = name;
    }

    @Override
    protected String getRequestMethod() {
        return METHOD_POST;
    }

    @Override
    protected String getMethod() {
        return "/v1/instances/";
    }

    @Override
    protected Type getResultType() {
        return Instance.class;
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
}
