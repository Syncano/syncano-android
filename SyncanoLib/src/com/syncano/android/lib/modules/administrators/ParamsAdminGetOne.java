package com.syncano.android.lib.modules.administrators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Gets admin info with specific id or email from current instance. admin_id/admin_email parameter means that one can
 * use either one of them - admin_id or admin_email.
 */
public class ParamsAdminGetOne extends Params {
	/** admin id or email */
	@Expose
	@SerializedName(value = "admin_email")
	private String adminEmail;
	/** admin id or email */
	@Expose
	@SerializedName(value = "admin_id")
	private String adminId;

	/**
	 * 
	 * @param adminEmail
	 *            admin id or email
	 * @param adminId
	 *            admin id or email
	 */
	public ParamsAdminGetOne(String adminEmail, String adminId) {
		setAdminEmail(adminEmail);
		setAdminId(adminId);
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseAdminGetOne();
	}

	@Override
	public String getMethodName() {
		return "admin.get_one";
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

}