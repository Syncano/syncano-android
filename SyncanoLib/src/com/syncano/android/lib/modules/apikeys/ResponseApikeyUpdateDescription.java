package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.ApiKey;

public class ResponseApiKeyUpdateDescription extends Response {
	/** ApiKey from response */
	@Expose
    @SerializedName(value = "apikey")
	private ApiKey apiKey;

	/**
	 * Gets ApiKey from response
	 * 
	 * @return apiKey
	 */
	public ApiKey getApiKey() {
		return apiKey;
	}

	/**
	 * Sets ApiKey
	 * 
	 * @param apiKey
	 *            ApiKey to set
	 */
	public void setApiKey(ApiKey apiKey) {
		this.apiKey = apiKey;
	}
}