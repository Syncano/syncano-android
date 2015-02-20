package com.syncano.android.lib.api.instances;

import com.google.gson.reflect.TypeToken;
import com.syncano.android.lib.api.BaseGetMethod;
import com.syncano.android.lib.data.Instance;
import com.syncano.android.lib.data.Page;

import java.lang.reflect.Type;

public class InstancesGet extends BaseGetMethod<Page<Instance>> {

    @Override
    protected String getMethod() { return "/v1/instances/"; }

    @Override
    protected Type getResultType() { return (new TypeToken<Page<Instance>>() {}).getType(); }
}
