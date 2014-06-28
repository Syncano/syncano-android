package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get one project.
 */
public class ParamsProjectGetOne extends Params {
	/** id of project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsProjectGetOne(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String getMethodName() {
		return "project.get_one";
	}

	public Response instantiateResponse() {
		return new ResponseProjectGetOne();
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