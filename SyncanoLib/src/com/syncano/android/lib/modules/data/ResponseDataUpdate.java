package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response with updated data
 */
public class ResponseDataUpdate extends Response {
	/** updated data */
	@Expose
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