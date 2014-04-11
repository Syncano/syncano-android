package com.syncano.android.lib.modules.notification;


import java.util.Date;

import com.syncano.android.lib.modules.Params;

/**
 * Params that gets specified collection history of notifications of current or specified client. History items are
 * stored for 24 hours.
 */
public class ParamsNotificationGetHistory extends Params {
	/** Clients id */
	private String client_id;
	/** Since id */
	private Integer since_id;
	/** Since time date */
	private Date since_time;
	/** limit for notifications history */
	private Integer limit;
	/** order for notifications history */
	private String order;

	@Override
	public String getMethodName() {
		return "notification.get_history";
	}

	/**
	 * @return client id
	 */
	public String getClient_id() {
		return client_id;
	}

	/**
	 * Sets client id
	 * 
	 * @param client_id
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	/**
	 * @return since id
	 */
	public Integer getSince_id() {
		return since_id;
	}

	/**
	 * Sets since id
	 * 
	 * @param since_id
	 */
	public void setSince_id(Integer since_id) {
		this.since_id = since_id;
	}

	/**
	 * @return since time date
	 */
	public Date getSince_time() {
		return since_time;
	}

	/**
	 * Sets since time date
	 * 
	 * @param since_time
	 */
	public void setSince_time(Date since_time) {
		this.since_time = since_time;
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