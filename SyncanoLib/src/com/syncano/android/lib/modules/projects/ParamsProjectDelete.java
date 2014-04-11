package com.syncano.android.lib.modules.projects;

import com.syncano.android.lib.modules.Params;

/**
 * Params to delete specified project.
 */
public class ParamsProjectDelete extends Params {
	/** id of project */
	private String project_id;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsProjectDelete(String projectId) {
		this.project_id = projectId;
	}

	@Override
	public String getMethodName() {
		return "project.delete";
	}

	/**
	 * @return id of project
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