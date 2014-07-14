package com.syncano.android.lib.modules.roles;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Role;

/**
 * Response with list of all permission roles of current instance.
 */
public class ResponseRoleGet extends Response {
	/** Array of roles */
	@Expose
	private Role[] role;

	/**
	 * @return array of roles
	 */
	public Role[] getRoles() {
		return role;
	}

	/**
	 * Sets array of roles
	 * 
	 * @param role
	 */
	public void setRoles(Role[] role) {
		this.role = role;
	}

}