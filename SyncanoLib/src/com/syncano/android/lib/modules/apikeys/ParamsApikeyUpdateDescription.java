package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates specified API client's info.
 */
public class ParamsApikeyUpdateDescription extends Params {
	/** Id of desired client, can be null */
	private String client_id;
	/** Description to update */
	private String description;

	/**
	 * @param description
	 *            New API client's description to set.
	 */
	public ParamsApikeyUpdateDescription(String description) {
		setDescription(description);
	}

	@Override
	public String getMethodName() {
		return "apikey.update_description";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApikeyNew();
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
	 * @return Clients id
	 */
	public String getClientId() {
		return client_id;
	}

	/**
	 * @param clientId
	 *            API client id. If not specified, will update current client.
	 */
	public void setClientId(String clientId) {
		this.client_id = clientId;
	}

}