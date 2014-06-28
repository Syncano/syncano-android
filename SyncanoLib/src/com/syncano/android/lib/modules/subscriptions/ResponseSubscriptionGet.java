package com.syncano.android.lib.modules.subscriptions;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Subscription;

/**
 * Params to get subscription list.
 */
public class ResponseSubscriptionGet extends Response {
	/**
	 * List of subscriptions
	 */
	@Expose
	private Subscription[] subscription;

	/**
	 * @return list of subscriptions
	 */
	public Subscription[] getSubscription() {
		return subscription;
	}

	/**
	 * Sets subscription list
	 * 
	 * @param subscription
	 */
	public void setSubscription(Subscription[] subscription) {
		this.subscription = subscription;
	}
}