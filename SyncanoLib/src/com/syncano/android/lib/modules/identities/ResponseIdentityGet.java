package com.syncano.android.lib.modules.identities;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Identity;

/**
 * Response for getting identities
 */
public class ResponseIdentityGet extends Response {
	/** array of identities */
	@Expose
	private Identity[] identity;

	/**
	 * @return identities array
	 */
	public Identity[] getIdentity() {
		return identity;
	}

	/**
	 * Sets identities array
	 * 
	 * @param identity
	 */
	public void setIdentity(Identity[] identity) {
		this.identity = identity;
	}
}