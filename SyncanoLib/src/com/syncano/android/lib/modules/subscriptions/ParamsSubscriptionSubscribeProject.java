package com.syncano.android.lib.modules.subscriptions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to subscribe to project.
 */
public class ParamsSubscriptionSubscribeProject extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** Context to subscribe within */
	@Expose
	private String context;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsSubscriptionSubscribeProject(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String getMethodName() {
		return "subscription.subscribe_project";
	}

	/**
	 * Sets project id
	 * 
	 * @param project_id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return project id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * Sets context
	 * 
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return context
	 */
	public String getContext() {
		return context;
	}
}