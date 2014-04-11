package com.syncano.android.lib.modules;


import java.util.HashMap;

import com.syncano.android.lib.Constants;

public abstract class Params {
	// special place to put additional parameters
	private HashMap<String, String> otherParams = new HashMap<String, String>();
	/** Api key needed to connect to api */
	private String api_key;
	/** Session id of actual session, valid for two hours, renewed by sending heartbeat or any call to api */
	private String session_id;
	/** Actual timezone */
	private String timezone = Constants.HYDRA_TIMEZONE;

	/**
	 * Default method to init response
	 * 
	 * @return new instance of response
	 */

	public Response instantiateResponse() {
		return new Response();
	}

	/**
	 * Adds parameter to current object
	 * 
	 * @param key
	 *            key for new value
	 * @param value
	 */
	public void addParam(String key, String value) {
		otherParams.put(key, value);
	}

	/**
	 * Abstract method to get method name string used by api
	 */
	public abstract String getMethodName();

	/**
	 * @return returns api key
	 */
	public String getApiKey() {
		return api_key;
	}

	/**
	 * Sets api key
	 * 
	 * @param api_key
	 *            new api key
	 */
	public void setApiKey(String api_key) {
		this.api_key = api_key;
	}

	/**
	 * @return sessionid of current session
	 */
	public String getSession_id() {
		return session_id;
	}

	/**
	 * @param sessionId
	 *            sets session id of current session
	 */
	public void setSession_id(String sessionId) {
		this.session_id = sessionId;
	}

	/**
	 * @return preferred timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone
	 *            sets preferred timezone
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;

	}
}
