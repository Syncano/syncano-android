package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates specified API client's info.
 */
public class ParamsApiKeyUpdateDescription extends Params {
	/** Id of desired client, can be null */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;
	/** Description to update */
	@Expose
	private String description;

	/**
	 * @param description
	 *            New API client's description to set.
	 */
	public ParamsApiKeyUpdateDescription(String description) {
		setDescription(description);
	}

	@Override
	public String getMethodName() {
		return "apikey.update_description";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApiKeyNew();
	}

	/**
	 * @return Description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            New API client's description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return API clients id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * @param apiClientId
	 *            API client id. If not specified, will update current client.
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

}