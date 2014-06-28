package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to delete specified project.
 */
public class ParamsProjectDelete extends Params {
	/** id of project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsProjectDelete(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String getMethodName() {
		return "project.delete";
	}

	/**
	 * @return id of project
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