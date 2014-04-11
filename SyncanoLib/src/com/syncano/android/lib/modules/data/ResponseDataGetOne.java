package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response for data get one
 */
public class ResponseDataGetOne extends Response {
	/** Data fetched */
	private Data data;

	/**
	 * @return data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Sets data
	 * 
	 * @param data
	 */
	public void setData(Data data) {
		this.data = data;
	}

}