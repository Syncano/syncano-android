package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to create new user.
 */
public class ParamsUserNew extends Params {
	/** Name of user */
	private String user_name;
	/** Nickname of user */
	private String nick;
	/** Avatar base64 for user */
	private String avatar;

	/**
	 * @param userName
	 *            User name defining user. Can be <code>null</code>.
	 */
	public ParamsUserNew(String userName) {
		setUser_name(userName);
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
	public String getUser_name() {
		return user_name;
	}

	/**
	 * Sets user name
	 * 
	 * @param user_name
	 *            user name
	 */
	public void setUser_name(String user_name) {
		this.user_name = user_name;
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

}