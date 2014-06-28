package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;

/**
 * Response with data count
 */
public class ResponseDataCount extends Response {
	/** Data count */
	@Expose
	private Integer count;

	/**
	 * Sets count
	 * 
	 * @param count
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return count
	 */
	public Integer getCount() {
		return count;
	}
}