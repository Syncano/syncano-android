package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get all users
 */
public class ParamsUserGetAll extends Params {
	/** Since user id */
	private String since_id;
	/** Limit of users to get */
	private Integer limit;

	@Override
	public String getMethodName() {
		return "user.get_all";
	}

	public Response instantiateResponse() {
		return new ResponseUserGetAll();
	}

	/**
	 * @return since user id
	 */
	public String getSince_id() {
		return since_id;
	}

	/**
	 * Sets since user id
	 * 
	 * @param since_id
	 */
	public void setSince_id(String since_id) {
		this.since_id = since_id;
	}

	/**
	 * @return limit of users to get
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Sets limit of users to get
	 * 
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}