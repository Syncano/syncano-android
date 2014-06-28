package com.syncano.android.lib.modules.notification;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params that gets specified collection history of notifications of current or specified client. History items are
 * stored for 24 hours.
 */
public class ParamsNotificationGetHistory extends Params {
	/** API Clients id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;
	/** Since id */
	@Expose
	@SerializedName(value = "since_id")
	private Integer sinceId;
	/** Since time date */
	@Expose
	@SerializedName(value = "since_time")
	private Date sinceTime;
	/** limit for notifications history */
	@Expose
	private Integer limit;
	/** order for notifications history */
	@Expose
	private String order;

	@Override
	public String getMethodName() {
		return "notification.get_history";
	}

	/**
	 * @return API Client id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * Sets API Client id
	 * 
	 * @param client_id
	 */
	public void setApiClientId(String clientId) {
		this.apiClientId = clientId;
	}

	/**
	 * @return since id
	 */
	public Integer getSinceId() {
		return sinceId;
	}

	/**
	 * Sets since id
	 * 
	 * @param since_id
	 */
	public void setSinceId(Integer sinceId) {
		this.sinceId = sinceId;
	}

	/**
	 * @return since time date
	 */
	public Date getSinceTime() {
		return sinceTime;
	}

	/**
	 * Sets since time date
	 * 
	 * @param since_time
	 */
	public void setSinceTime(Date sinceTime) {
		this.sinceTime = sinceTime;
	}

	/**
	 * @return limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Sets limit
	 * 
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * @return order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * Sets order
	 * 
	 * @param order
	 */
	public void setOrder(String order) {
		this.order = order;
	}

}