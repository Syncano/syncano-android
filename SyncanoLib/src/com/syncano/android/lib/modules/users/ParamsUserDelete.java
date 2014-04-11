package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Params;

/**
 * Params to delete specified user
 */
public class ParamsUserDelete extends Params {
	/** Id of user */
	private String user_id;
	/** Name of user */
	private String user_name;

	/**
	 * @param userId
	 *            User id defining user. Can be <code>null</code>.
	 * @param userName
	 *            User name defining user. Can be <code>null</code>.
	 */
	public ParamsUserDelete(String userId, String userName) {
		this.user_id = userId;
		this.user_name = userName;
	}

	@Override
	public String getMethodName() {
		return "user.delete";
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

}