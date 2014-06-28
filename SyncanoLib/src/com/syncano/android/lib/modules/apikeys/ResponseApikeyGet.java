package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.ApiKey;

public class ResponseApiKeyGet extends Response {
	/** ApiKeys array from response */
	@Expose
    @SerializedName(value = "apikey")
	private ApiKey[] apiKey;

	/**
	 * Gets ApiKeys array from response
	 * 
	 * @return apiKeys
	 */
	public ApiKey[] getApiKey() {
		return apiKey;
	}

	/**
	 * Sets ApiKeys array
	 * 
	 * @param apiKey
	 *            ApiKeys array to set
	 */
	public void setApiKey(ApiKey[] apiKey) {
		this.apiKey = apiKey;
	}
}