package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Data;

/**
 * Response for creating new data
 */
public class ResponseDataNew extends Response {
	/** New data */
	@Expose
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