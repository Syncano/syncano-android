package com.syncano.android.lib.modules.subscriptions;

import com.syncano.android.lib.modules.Params;

/**
 * Params that unsubscribes from project subscription.
 */
public class ParamsSubscriptionUnsubscribeProject extends Params {
	/** id of specified project */
	private String project_id;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsSubscriptionUnsubscribeProject(String projectId) {
		this.project_id = projectId;
	}

	@Override
	public String getMethodName() {
		return "subscription.unsubscribe_project";
	}

	/**
	 * @return project id
	 */
	public String getProject_id() {
		return project_id;
	}

	/**
	 * Sets project id
	 * 
	 * @param project_id
	 */
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
}