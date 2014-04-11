package com.syncano.android.lib.modules.projects;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to update existing project.
 */
public class ParamsProjectUpdate extends Params {
	/** id of project */
	private String project_id;
	/** name */
	private String name;
	/** description */
	private String description;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsProjectUpdate(String projectId) {
		this.project_id = projectId;
	}

	@Override
	public String getMethodName() {
		return "project.update";
	}

	public Response instantiateResponse() {
		return new ResponseProjectUpdate();
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

	/**
	 * @return project name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets project name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return project description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}