package com.syncano.android.lib.modules.subscriptions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params gets all subscriptions for specified user.
 */
public class ParamsSubscriptionGet extends Params {
	/** API Clients id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;

	/**
	 * Default constructor, needs at least one parameter, login or id.
	 * 
	 * @param apiClientId
	 *            Id of API Client. Can be <code>null</code>.
	 */
	public ParamsSubscriptionGet(String apiClientId) {
		this.apiClientId = apiClientId;
	}

	@Override
	public String getMethodName() {
		return "subscription.get";
	}

	public Response instantiateResponse() {
		return new ResponseSubscriptionGet();
	}

	/**
	 * @return API Clients id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * Sets API Clients id
	 * 
	 * @param apiClientId
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

}