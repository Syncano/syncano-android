package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Creates a new API key client in current instance. Only Admin permission role can create new API clients.
 */
public class ParamsApikeyNew extends Params {
	/** Role id */
	private String role_id;
	/** Description */
	private String description;

	/**
	 * @param roleId
	 *            New API client's permission role id (see role.get()).
	 * @param description
	 *            Description of new API client.
	 */
	public ParamsApikeyNew(String roleId, String description) {
		setRoleId(roleId);
		setDescription(description);
	}

	@Override
	public String getMethodName() {
		return "apikey.new";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApikeyNew();
	}

	/**
	 * @return API client's permission role id
	 */
	public String getRoleId() {
		return role_id;
	}

	/**
	 * Sets role id
	 * 
	 * @param roleId
	 */
	public void setRoleId(String roleId) {
		this.role_id = roleId;
	}

	/**
	 * @return Description of new API client.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets new description of new API client.
	 * 
	 * @param description
	 *            new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}