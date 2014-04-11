package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to update specified user.
 */
public class ParamsUserUpdate extends Params {
	/** Id of user */
	private String user_id;
	/** Name of user */
	private String user_name;
	/** Nickname of user */
	private String nick;
	/** Avatar url for user */
	private String avatar;

	/**
	 * Default constructor, needs at least one parameter, userId or userName. If both id and name are specified, will
	 * use id for getting user while user_name will be updated with provided new value.
	 * 
	 * @param userId
	 *            User id defining user. Can be <code>null</code>.
	 * @param userName
	 *            User name defining user. Can be <code>null</code>.
	 */
	public ParamsUserUpdate(String userId, String userName) {
		this.user_id = userId;
		this.user_name = userName;
	}

	@Override
	public String getMethodName() {
		return "user.update";
	}

	public Response instantiateResponse() {
		return new ResponseUserUpdate();
	}

	/**
	 * @return user id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * Sets user id
	 * 
	 * @param user_id
	 *            if for user
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	 * @return avatar url
	 */
	public String getAvatar() {
		return avatar;
	}

	/**
	 * Sets avatar url
	 * 
	 * @param avatar
	 *            avatar url
	 */
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}