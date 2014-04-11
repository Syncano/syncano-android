package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response with copied data
 */
public class ResponseDataCopy extends Response {
	/** copied data */
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
