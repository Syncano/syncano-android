package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Deletes specified API client. Only Admin permission role can delete API clients.
 */
public class ParamsApiKeyDelete extends Params {
	/** API id of client */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;

	/**
	 * @param apiClientId
	 *            API client id defining API client to delete.
	 */
	public ParamsApiKeyDelete(String clientId) {
		setApiClientId(clientId);
	}

	@Override
	public String getMethodName() {
		return "apikey.delete";
	}

	/**
	 * Returns API client id
	 * 
	 * @return id of client
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * Sets id for desired API client
	 * 
	 * @param apiClientId
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

}