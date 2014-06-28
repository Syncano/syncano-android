package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.User;

/**
 * Response contains one user that fits Param criteria
 */
public class ResponseUserGetOne extends Response {
	/** User object */
	@Expose
	private User user;

	/**
	 * @return user object
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets new user
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}
}