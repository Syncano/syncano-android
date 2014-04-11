package com.syncano.android.lib.modules.apikeys;

import com.syncano.android.lib.modules.Response;

public class ResponseApikeyStartSession extends Response {
	/** Session id */
	private String session_id;
	/** UUID */
	private String uuid;

	/**
	 * @return session id for current session
	 */
	public String getSessionId() {
		return session_id;
	}

	/**
	 * Sets new session id
	 * 
	 * @param sessionId
	 *            new session id
	 */
	public void setSessionId(String sessionId) {
		this.session_id = sessionId;
	}

	/**
	 * @return UUID for response
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets new UUID
	 * 
	 * @param uuid
	 *            new UUID
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

}