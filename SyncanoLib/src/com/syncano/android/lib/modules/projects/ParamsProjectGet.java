package com.syncano.android.lib.modules.projects;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get projects.
 */

public class ParamsProjectGet extends Params {

	@Override
	public String getMethodName() {
		return "project.get";
	}

	public Response instantiateResponse() {
		return new ResponseProjectGet();
	}
}