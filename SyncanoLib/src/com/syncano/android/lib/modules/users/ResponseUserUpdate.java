package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.User;

/**
 * Response containing user after update
 */
public class ResponseUserUpdate extends Response {
	/** updated user */
	private User user;

	/**
	 * @return updated user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets new updated user
	 * 
	 * @param user
	 *            updated user
	 */
	public void setUser(User user) {
		this.user = user;
	}
}