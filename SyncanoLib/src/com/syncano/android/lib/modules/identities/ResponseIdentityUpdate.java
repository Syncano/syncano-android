package com.syncano.android.lib.modules.identities;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Identity;

/**
 * Response for identity update
 */
public class ResponseIdentityUpdate extends Response {
	/** Updated identity */
	@Expose
	private Identity identity;

	/**
	 * @return updated identity
	 */
	public Identity getIdentity() {
		return identity;
	}

	/**
	 * Sets updated identity
	 * 
	 * @param identity
	 */
	public void setIdentity(Identity identity) {
		this.identity = identity;
	}
}