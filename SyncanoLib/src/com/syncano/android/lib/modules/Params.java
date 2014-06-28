package com.syncano.android.lib.modules;

import java.util.HashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.Constants;

public abstract class Params {
	// special place to put additional parameters
	// this parameter is not exposed but it is added to request manually, see GsonHelper
	private HashMap<String, String> otherParams = new HashMap<String, String>();
	/** Api key needed to connect to api */
	@Expose
	@SerializedName(value = "api_key")
	private String apiKey;
	/** User authorization key */
	@Expose
	@SerializedName(value = "auth_key")
	private String authKey;
	/** Session id of actual session, valid for two hours, renewed by sending heartbeat or any call to api */
	@Expose
	@SerializedName(value = "session_id")
	private String sessionId;
	/** Actual timezone */
	@Expose
	private String timezone = Constants.DEFAULT_TIMEZONE;

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

	public HashMap<String, String> getAdditionalParams() {
		return otherParams;
	}

	/**
	 * Abstract method to get method name string used by api
	 */
	public abstract String getMethodName();

	/**
	 * @return returns api key
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets api key
	 * 
	 * @param api_key
	 *            new api key
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return user authorization key
	 */
	public String getAuthKey() {
		return authKey;
	}

	/**
	 * @param Sets user authorization key
	 */
	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	/**
	 * @return sessionid of current session
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId
	 *            sets session id of current session
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
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

	public Response instantiateResponse() {
		return new Response();
	}
}
