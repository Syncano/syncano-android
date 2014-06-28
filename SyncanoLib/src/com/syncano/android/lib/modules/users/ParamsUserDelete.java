package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to delete specified user
 */
public class ParamsUserDelete extends Params {
	/** Id of user */
	@Expose
	@SerializedName(value = "user_id")
	private String userId;
	/** Name of user */
	@Expose
	@SerializedName(value = "user_name")
	private String userName;

	/**
	 * @param userId
	 *            User id defining user. Can be <code>null</code>.
	 * @param userName
	 *            User name defining user. Can be <code>null</code>.
	 */
	public ParamsUserDelete(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	@Override
	public String getMethodName() {
		return "user.delete";
	}

	/**
	 * @return user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets user id
	 * 
	 * @param user_id
	 *            if for user
	 */
	public void setUserId(String userId) {
		this.userId = userId;
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

}