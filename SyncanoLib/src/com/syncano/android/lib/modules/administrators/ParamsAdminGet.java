package com.syncano.android.lib.modules.administrators;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get all administrators of current instance.
 */
public class ParamsAdminGet extends Params {

	public ParamsAdminGet() {
	}

	@Override
	public String getMethodName() {
		return "admin.get";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseAdminGet();
	}
}