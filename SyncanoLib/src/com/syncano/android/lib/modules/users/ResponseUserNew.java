package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.User;

/**
 * Response containing new user
 */
public class ResponseUserNew extends Response {
	/** new user */
	@Expose
	private User user;

	/**
	 * @return new user
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