package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Removes permission from specified User API client. Requires Backend API key with Admin permission role.
 */
public class ParamsApiKeyDeauthorize extends Params {
	/** api client id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;
	/** permission */
	@Expose
	private String permission;

	public ParamsApiKeyDeauthorize(String apiClientId, String permission) {
		setApiClientId(apiClientId);
		setPermission(permission);
	}

	@Override
	public String getMethodName() {
		return "apikey.deauthorize";
	}

	/**
	 * @return api client id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * @param Sets
	 *            api client id
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

	/**
	 * @return permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param Sets
	 *            permission
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}
}
