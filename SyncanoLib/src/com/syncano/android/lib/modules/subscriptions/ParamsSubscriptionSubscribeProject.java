package com.syncano.android.lib.modules.subscriptions;

import com.syncano.android.lib.modules.Params;

/**
 * Params to subscribe to project.
 */
public class ParamsSubscriptionSubscribeProject extends Params {
	/** id of specified project */
	private String project_id;
	/** Context to subscribe within */
	private String context;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsSubscriptionSubscribeProject(String projectId) {
		this.project_id = projectId;
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
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	/**
	 * @return project id
	 */
	public String getProject_id() {
		return project_id;
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