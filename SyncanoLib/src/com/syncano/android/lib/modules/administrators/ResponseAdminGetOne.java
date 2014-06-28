package com.syncano.android.lib.modules.administrators;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Admin;

/**
 * Response with administrator specified by params
 */
public class ResponseAdminGetOne extends Response {
	/** Administrator */
	@Expose
	private Admin admin;

	/**
	 * @return administrator
	 */
	public Admin getAdmin() {
		return admin;
	}

	/**
	 * Sets administrator
	 * 
	 * @param admin
	 *            administrator
	 */
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}