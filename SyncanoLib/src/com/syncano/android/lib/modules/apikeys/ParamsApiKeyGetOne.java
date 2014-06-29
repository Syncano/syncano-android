package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Gets info of one specified API client. Only Admin permission role can view other API clients.
 */
public class ParamsApiKeyGetOne extends Params {
	/** API client id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;

	/**
	 * Default constructor
	 */
	public ParamsApiKeyGetOne() {
	}

	@Override
	public String getMethodName() {
		return "apikey.get_one";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApiKeyGetOne();
	}

	/**
	 * @return current API client id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * @param apiClientId
	 *            API client id. If not specified, will use current client.
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}
}