package com.syncano.android.lib.modules.roles;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Role;

/**
 * Response with list of all permission roles of current instance.
 */
public class ResponseRoleGet extends Response {
	/** Array of roles */
	private Role[] roles;

	/**
	 * @return array of roles
	 */
	public Role[] getRoles() {
		return roles;
	}

	/**
	 * Sets array of roles
	 * 
	 * @param roles
	 */
	public void setRoles(Role[] roles) {
		this.roles = roles;
	}

}