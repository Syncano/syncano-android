package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to update specified user.
 */
public class ParamsUserUpdate extends Params {
	/** Id of user */
	@Expose
	@SerializedName(value = "user_id")
	private String userId;
	/** Name of user */
	@Expose
	@SerializedName(value = "user_name")
	private String userName;
	/** Nickname of user */
	@Expose
	private String nick;
	/** Avatar url for user */
	@Expose
	private String avatar;
	/** New user password. */
	@Expose
	@SerializedName(value = "new_password")
	private String newPassword;
	/** Current user password. */
	@Expose
	@SerializedName(value = "current_password")
	private String currentPassword;

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
		this.userId = userId;
		this.userName = userName;
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

	/**
	 * @return new password
	 */
	public String getNewPassword() {
		return newPassword;
	}

	/**
	 * @param Sets
	 *            new user password
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return current password
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}

	/**
	 * @param Sets
	 *            current user password
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
}