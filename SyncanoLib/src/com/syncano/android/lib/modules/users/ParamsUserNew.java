package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to create new user.
 */
public class ParamsUserNew extends Params {
	/** Name of user */
	@Expose
	@SerializedName(value = "user_name")
	private String userName;
	/** Nickname of user */
	@Expose
	private String nick;
	/** Avatar base64 for user */
	@Expose
	private String avatar;
	/** User's password. */
	@Expose
	@SerializedName(value = "password")
	private String password;
	
	/**
	 * @param userName
	 *            User name defining user. Can be <code>null</code>.
	 */
	public ParamsUserNew(String userName) {
		setUserName(userName);
	}

	@Override
	public String getMethodName() {
		return "user.new";
	}

	public Response instantiateResponse() {
		return new ResponseUserNew();
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
	 * @return user nickname
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets user nickname
	 * 
	 * @param nick
	 *            nickname
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return avatar base64
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Sets avatar base64
	 * 
	 * @param avatar
	 *            avatar base64
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param Sets
	 *            user password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}