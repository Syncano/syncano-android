package com.syncano.android.lib.modules.identities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get currently connected API client identities up to limit (max 100).
 */
public class ParamsIdentityGet extends Params {

	/** client id of identity */
	@Expose
	@SerializedName(value = "client_id")
	private String clientId;
	/** name */
	@Expose
	private String name;
	/** since id */
	@Expose
	@SerializedName(value = "since_id")
	private String sinceId;
	/** limit of identities */
	@Expose
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
		return clientId;
	}

	/**
	 * Sets client id
	 * 
	 * @param clientId
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
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
		return sinceId;
	}

	/**
	 * Sets since id
	 * 
	 * @param sinceId
	 */
	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
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