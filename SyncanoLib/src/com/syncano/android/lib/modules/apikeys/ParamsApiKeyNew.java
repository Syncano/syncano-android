package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Creates a new API key client in current instance. Only Admin permission role can create new API clients.
 */
public class ParamsApiKeyNew extends Params {
	/**
	 * Type of new API client. Possible values:
	 * <ul>
	 * <li>backend (default) - API key that is not user-aware and has global permissions,</li>
	 * <li>user - user-aware API key that can define per container permissions.</li>
	 * </ul>
	 */
	@Expose
	private String type;
	/** Role id */
	@Expose
	@SerializedName(value = "role_id")
	private String roleId;
	/** Description */
	@Expose
	private String description;

	/**
	 * 
	 * @param description
	 *            Description of new API client.
	 * @param type
	 *            Type of new API client.
	 */
	public ParamsApiKeyNew(String description, String type) {
		setDescription(description);
		setType(type);
	}

	/**
	 * 
	 * @param description
	 *            Description of new API client.
	 */
	public ParamsApiKeyNew(String description) {
		setDescription(description);
	}

	@Override
	public String getMethodName() {
		return "apikey.new";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApiKeyNew();
	}

	/**
	 * @return Type of new API client
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type of new API client. Possible values:
	 * <ul>
	 * <li>backend (default) - API key that is not user-aware and has global permissions,</li>
	 * <li>user - user-aware API key that can define per container permissions.</li>
	 * </ul>
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return API client's permission role id
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * Sets role id
	 * 
	 * @param roleId
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
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