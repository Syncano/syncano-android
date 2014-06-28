package com.syncano.android.lib.modules.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params that sends custom notification to client through Sync Server.
 */
public class ParamsNotificationSend extends Params {
	/** API Clients id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;
	/** uuid */
	@Expose
	private String uuid;

	/**
	 * Default constructor, needs at least one parameter, login or id.
	 * 
	 * @param clientLogin
	 *            Login of client. Can be <code>null</code>.
	 * @param apiClientId
	 *            Id of API Client. Can be <code>null</code>.
	 */
	public ParamsNotificationSend(String apiClientId) {
		this.apiClientId = apiClientId;
	}

	@Override
	public String getMethodName() {
		return "notification.send";
	}

	/**
	 * @return API Client id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * Sets API Client id
	 * 
	 * @param apiClientId
	 */
	public void setClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

	/**
	 * @return uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets uuid
	 * 
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}