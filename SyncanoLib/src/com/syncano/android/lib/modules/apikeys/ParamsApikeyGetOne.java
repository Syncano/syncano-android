package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Gets info of one specified API client. Only Admin permission role can view other API clients.
 */
public class ParamsApikeyGetOne extends Params {
	/** Client id */
	private String client_id;

	/**
	 * Default constructor
	 */
	public ParamsApikeyGetOne() {
	}

	@Override
	public String getMethodName() {
		return "apikey.get_one";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseApikeyGetOne();
	}

	/**
	 * @return current client id
	 */
	public String getClientId() {
		return client_id;
	}

	/**
	 * @param clientId
	 *            API client id. If not specified, will use current client.
	 */
	public void setClientId(String clientId) {
		this.client_id = clientId;
	}
}