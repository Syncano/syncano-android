package com.syncano.android.lib.modules.notification;

import com.syncano.android.lib.modules.Params;

/**
 * Params that sends custom notification to client through Sync Server.
 */
public class ParamsNotificationSend extends Params {
	/** Clients id */
	private String client_id;
	/** uuid */
	private String uuid;

	/**
	 * Default constructor, needs at least one parameter, login or id.
	 * 
	 * @param clientLogin
	 *            Login of client. Can be <code>null</code>.
	 * @param clientId
	 *            Id of client. Can be <code>null</code>.
	 */
	public ParamsNotificationSend(String clientId) {
		client_id = clientId;
	}

	@Override
	public String getMethodName() {
		return "notification.send";
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
	 * @return uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets uuid
	 * 
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}