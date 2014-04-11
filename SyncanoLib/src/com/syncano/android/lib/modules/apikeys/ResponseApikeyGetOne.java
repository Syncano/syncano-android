package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Client;

public class ResponseApikeyGetOne extends Response {
	/** client from response */
	private Client client;

	/**
	 * Gets client from response
	 * 
	 * @return client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * Sets client
	 * 
	 * @param client
	 *            client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}
}