package com.syncano.android.lib.modules.subscriptions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params that unsubscribes from project subscription.
 */
public class ParamsSubscriptionUnsubscribeProject extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsSubscriptionUnsubscribeProject(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String getMethodName() {
		return "subscription.unsubscribe_project";
	}

	/**
	 * @return project id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * Sets project id
	 * 
	 * @param project_id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}