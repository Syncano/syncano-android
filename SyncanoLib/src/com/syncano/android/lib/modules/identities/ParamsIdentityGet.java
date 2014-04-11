package com.syncano.android.lib.modules.identities;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get currently connected API client identities up to limit (max 100).
 */
public class ParamsIdentityGet extends Params {

	/** client id of identity */
	private String client_id;
	/** name */
	private String name;
	/** since id */
	private String since_id;
	/** limit of identities */
	private String limit;

	@Override
	public String getMethodName() {
		return "identity.get";
	}

	public Response instantiateResponse() {
		return new ResponseIdentityGet();
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
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return since id
	 */
	public String getSinceId() {
		return since_id;
	}

	/**
	 * Sets since id
	 * 
	 * @param sinceId
	 */
	public void setSinceId(String sinceId) {
		this.since_id = sinceId;
	}

	/**
	 * @return limit
	 */
	public String getLimit() {
		return limit;
	}

	/**
	 * Sets limit
	 * 
	 * @param limit
	 */
	public void setLimit(String limit) {
		this.limit = limit;
	}

}