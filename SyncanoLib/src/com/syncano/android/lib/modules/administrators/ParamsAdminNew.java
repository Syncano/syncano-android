package com.syncano.android.lib.modules.administrators;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Adds a new administrator to current instance. Account with admin_email must exist in Syncano. Only Admin permission
 * role can add new administrators.
 */
public class ParamsAdminNew extends Params {
	/** Email of admin to add */
	private String admin_email;
	/** Initial role for current instance (see role.get()) */
	private String role_id;

	/**
	 * 
	 * @param adminEmail
	 *            Email of admin to add
	 * @param roleId
	 *            Initial role for current instance (see role.get())
	 */
	public ParamsAdminNew(String adminEmail, String roleId) {
		setAdminEmail(adminEmail);
		setRoleId(roleId);
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseAdminNew();
	}

	@Override
	public String getMethodName() {
		return "admin.new";
	}

	/**
	 * @return email of desired administrator
	 */
	public String getAdminEmail() {
		return admin_email;
	}

	/**
	 * Sets new email for administrator
	 * 
	 * @param adminEmail
	 *            email of administrator
	 */
	public void setAdminEmail(String adminEmail) {
		this.admin_email = adminEmail;
	}

	/**
	 * @return role id of desired administrator
	 */
	public String getRoleId() {
		return role_id;
	}

	/**
	 * Sets new role id for administrator
	 * 
	 * @param roleId
	 *            role id of administrator
	 */
	public void setRoleId(String roleId) {
		this.role_id = roleId;
	}

}