package com.syncano.android.lib.modules.projects;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get one project.
 */
public class ParamsProjectGetOne extends Params {
	/** id of project */
	private String project_id;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsProjectGetOne(String projectId) {
		this.project_id = projectId;
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