package com.syncano.android.lib.modules.administrators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates specified admin's permission role. Only Admin permission role can edit administrators. admin_id/admin_email
 * parameter means that one can use either one of them - admin_id or admin_email.
 */
public class ParamsAdminUpdate extends Params {
	/** Id of admin to add */
	@Expose
	@SerializedName(value = "admin_id")
	private String adminId;
	/** Email of admin to add */
	@Expose
	@SerializedName(value = "admin_email")
	private String adminEmail;
	/** Initial role for current instance (see role.get()) */
	@Expose
	@SerializedName(value = "role_id")
	private String roleId;

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
		return adminEmail;
	}

	/**
	 * Sets new email for administrator
	 * 
	 * @param adminEmail
	 *            email of administrator
	 */
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	/**
	 * @return id of desired administrator
	 */
	public String getAdminId() {
		return adminId;
	}

	/**
	 * Sets new id for administrator
	 * 
	 * @param adminId
	 *            id of administrator
	 */
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	/**
	 * @return role id of desired administrator
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * Sets new role id for administrator
	 * 
	 * @param roleId
	 *            role id of administrator
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}