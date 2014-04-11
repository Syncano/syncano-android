package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Params;

/**
 * Deletes specified API client. Only Admin permission role can delete API clients.
 */
public class ParamsApikeyDelete extends Params {
	/** id of client */
	private String client_id;

	/**
	 * @param clientId
	 *            API client id defining API client to delete.
	 */
	public ParamsApikeyDelete(String clientId) {
		setClientId(clientId);
	}

	@Override
	public String getMethodName() {
		return "apikey.new";
	}

	/**
	 * Returns client id
	 * 
	 * @return id of client
	 */
	public String getClientId() {
		return client_id;
	}

	/**
	 * Sets id for desired client
	 * 
	 * @param clientId
	 */
	public void setClientId(String clientId) {
		this.client_id = clientId;
	}

}