package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response with data array
 */
public class ResponseDataGet extends Response {
	/** Fetched data array */
	private Data[] data;

	/**
	 * @return data array
	 */
	public Data[] getData() {
		return data;
	}

	/**
	 * Sets data array
	 * 
	 * @param data
	 */
	public void setData(Data[] data) {
		this.data = data;
	}
}