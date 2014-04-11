package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Response;

/**
 * Response with count of all users
 */
public class ResponseUserCount extends Response {
	/** count of all users */
	private Integer count;

	/**
	 * @return users count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * Sets users count
	 * 
	 * @param count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
}
