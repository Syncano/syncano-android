package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response for creating new data
 */
public class ResponseDataNew extends Response {
	/** New data */
	private Data data;

	/**
	 * @return new data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Sets new data
	 * 
	 * @param data
	 */
	public void setData(Data data) {
		this.data = data;
	}

}