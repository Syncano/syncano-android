package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.User;

/**
 * Response contains array of all users
 */
public class ResponseUserGetAll extends Response {
	/** Array of users */
	private User[] user;

	/**
	 * @return array of all users
	 */
	public User[] getUser() {
		return user;
	}

	/**
	 * Sets array of users
	 * 
	 * @param user
	 */
	public void setUser(User[] user) {
		this.user = user;
	}
}