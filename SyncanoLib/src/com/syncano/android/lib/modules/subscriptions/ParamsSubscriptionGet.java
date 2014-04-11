package com.syncano.android.lib.modules.subscriptions;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params gets all subscriptions for specified user.
 */
public class ParamsSubscriptionGet extends Params {
	/** Clients id */
	private String client_id;

	/**
	 * Default constructor, needs at least one parameter, login or id.
	 * 
	 * @param clientId
	 *            Id of client. Can be <code>null</code>.
	 */
	public ParamsSubscriptionGet(String clientId) {
		client_id = clientId;
	}

	@Override
	public String getMethodName() {
		return "subscription.get";
	}

	public Response instantiateResponse() {
		return new ResponseSubscriptionGet();
	}

	/**
	 * @return Clients id
	 */
	public String getClient_id() {
		return client_id;
	}

	/**
	 * Sets clients id
	 * 
	 * @param client_id
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

}