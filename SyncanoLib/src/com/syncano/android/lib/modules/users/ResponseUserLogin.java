package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Response;

/**
 * Response containing user auth_key
 */
public class ResponseUserLogin extends Response {
	/** user authorization key */
	@Expose
	@SerializedName(value = "auth_key")
	private String authKey;

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

	
}