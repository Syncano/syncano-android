package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get all users
 */
public class ParamsUserGetAll extends Params {
	/** Since user id */
	@Expose
	@SerializedName(value = "since_id")
	private String sinceId;
	/** Limit of users to get */
	@Expose
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
	public String getSinceId() {
		return sinceId;
	}

	/**
	 * Sets since user id
	 * 
	 * @param since_id
	 */
	public void setSinceId(String sinceId) {
		this.sinceId = sinceId;
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