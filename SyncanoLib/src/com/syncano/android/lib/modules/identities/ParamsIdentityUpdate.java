package com.syncano.android.lib.modules.identities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates specified API client identity.
 */
public class ParamsIdentityUpdate extends Params {
	/** uuid */
	@Expose
	private String uuid;
	/** state of identity */
	@Expose
	private String state;
	/** API Client id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;

	/**
	 * @param uuid
	 *            uuid of identity
	 * @param state
	 *            state of identity
	 */
	public ParamsIdentityUpdate(String uuid, String state) {
		setUuid(uuid);
		setState(state);
	}

	@Override
	public String getMethodName() {
		return "identity.update";
	}

	public Response instantiateResponse() {
		return new ResponseIdentityUpdate();
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
	public void setApiClientId(String apiClientId) {
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

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

}