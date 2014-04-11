package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response with updated data
 */
public class ResponseDataUpdate extends Response {
	/** updated data */
	private Data data;

	/**
	 * @return updated data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Sets updated data
	 * 
	 * @param data
	 */
	public void setData(Data data) {
		this.data = data;
	}
}