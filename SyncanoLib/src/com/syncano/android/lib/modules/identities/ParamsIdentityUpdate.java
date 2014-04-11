package com.syncano.android.lib.modules.identities;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates specified API client identity.
 */
public class ParamsIdentityUpdate extends Params {
	/** uuid */
	private String uuid;
	/** state of identity */
	private String state;
	/** client id */
	private String client_id;

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
	 * @return client id
	 */
	public String getClientId() {
		return client_id;
	}

	/**
	 * Sets client id
	 * 
	 * @param clientId
	 */
	public void setClient_id(String clientId) {
		this.client_id = clientId;
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