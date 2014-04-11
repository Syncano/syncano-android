package com.syncano.android.lib.modules.administrators;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Gets admin info with specific id or email from current instance. admin_id/admin_email parameter means that one can
 * use either one of them - admin_id or admin_email.
 */
public class ParamsAdminGetOne extends Params {
	/** admin id or email */
	private String admin_email;
	/** admin id or email */
	private String admin_id;

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

}