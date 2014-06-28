package com.syncano.android.lib.modules.administrators;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Admin;

/**
 * Response with array of administrators in project
 */
public class ResponseAdminGet extends Response {
	/** array of administrators */
	@Expose
	private Admin[] admin;

	/**
	 * @return Array containing all administrators
	 */
	public Admin[] getAdmin() {
		return admin;
	}

	/**
	 * Sets administrators array
	 * 
	 * @param admin
	 *            administrators array
	 */
	public void setAdmin(Admin[] admin) {
		this.admin = admin;
	}
}