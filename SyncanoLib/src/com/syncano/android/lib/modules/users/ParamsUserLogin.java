package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Logs in a user.
 * <p>
 * Note: This method is intended for User API key usage.
 * </p>
 */
public class ParamsUserLogin extends Params {
	/** user name */
	@Expose
	@SerializedName(value = "user_name")
	private String userName;
	/** password */
	@Expose
	private String password;

	public ParamsUserLogin(String userName, String password) {
		setUserName(userName);
		setPassword(password);
	}

	@Override
	public String getMethodName() {
		return "user.login";
	}

	public Response instantiateResponse() {
		return new ResponseUserLogin();
	}

	/**
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets user name
	 * 
	 * @param user_name
	 *            user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param Sets password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}