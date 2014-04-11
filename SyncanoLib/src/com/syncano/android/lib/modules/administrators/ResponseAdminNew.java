package com.syncano.android.lib.modules.administrators;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Admin;

/**
 * Response with new admin
 */
public class ResponseAdminNew extends Response {
	/** new admin */
	private Admin admin;

	/**
	 * @return new admin object
	 */
	public Admin getAdmin() {
		return admin;
	}

	/**
	 * Sets new admin
	 * 
	 * @param admin
	 *            new admin
	 */
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}

}