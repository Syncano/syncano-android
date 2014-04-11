package com.syncano.android.lib.modules.administrators;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Admin;

/**
 * Response containing administrator after update
 */
public class ResponseAdminUpdate extends Response {
	/** updated administrator returned from api */
	private Admin admin;

	/**
	 * @return updated administrator
	 */
	public Admin getAdmin() {
		return admin;
	}

	/**
	 * Sets updated administrator
	 * 
	 * @param admin
	 *            updated administrator
	 */
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}