package com.syncano.android.lib.modules.administrators;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates specified admin's permission role. Only Admin permission role can edit administrators. admin_id/admin_email
 * parameter means that one can use either one of them - admin_id or admin_email.
 */
public class ParamsAdminUpdate extends Params {
	/** Id of admin to add */
	private String admin_id;
	/** Email of admin to add */
	private String admin_email;
	/** Initial role for current instance (see role.get()) */
	private String role_id;

	/**
	 * 
	 * @param adminEmail
	 *            admin id or email defining admin to update
	 * @param adminId
	 *            admin id or email defining admin to update
	 * @param roleId
	 *            New admin's instance role id to set (see role.get())
	 */
	public ParamsAdminUpdate(String adminEmail, String adminId, String roleId) {
		setAdminEmail(adminEmail);
		setAdminId(adminId);
		setRoleId(roleId);
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseAdminUpdate();
	}

	@Override
	public String getMethodName() {
		return "admin.update";
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
	 * @return id of desired administrator
	 */
	public String getAdminId() {
		return admin_id;
	}

	/**
	 * Sets new id for administrator
	 * 
	 * @param adminId
	 *            id of administrator
	 */
	public void setAdminId(String adminId) {
		this.admin_id = adminId;
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