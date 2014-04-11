package com.syncano.android.lib.modules.roles;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Lists all permission roles of current instance.
 */
public class ParamsRoleGet extends Params {
	/**
	 * Default empty constructor
	 */
	public ParamsRoleGet() {
	}

	@Override
	public String getMethodName() {
		return "role.get";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseRoleGet();
	}
}