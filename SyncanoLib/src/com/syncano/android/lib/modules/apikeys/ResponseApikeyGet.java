package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Client;

public class ResponseApikeyGet extends Response {
	/** clients array from response */
	private Client[] client;

	/**
	 * Gets clients array from response
	 * 
	 * @return client
	 */
	public Client[] getClient() {
		return client;
	}

	/**
	 * Sets client array
	 * 
	 * @param client
	 *            clients array to set
	 */
	public void setClient(Client[] client) {
		this.client = client;
	}
}