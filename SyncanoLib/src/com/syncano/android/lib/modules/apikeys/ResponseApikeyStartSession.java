package com.syncano.android.lib.modules.apikeys;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Response;

public class ResponseApiKeyStartSession extends Response {
	/** Session id */
	@Expose
	@SerializedName(value = "session_id")
	private String sessionId;
	/** UUID */
	@Expose
	private String uuid;

	/**
	 * @return session id for current session
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets new session id
	 * 
	 * @param sessionId
	 *            new session id
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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