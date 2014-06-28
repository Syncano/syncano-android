package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.User;

/**
 * Response with array of users that fitted Param criteria
 */
public class ResponseUserGet extends Response {
	/** Array of users */
	@Expose
	private User[] user;

	/**
	 * @return array of all users that fitted Param criteria
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