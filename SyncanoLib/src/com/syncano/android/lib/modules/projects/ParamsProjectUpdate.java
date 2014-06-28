package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to update existing project.
 */
public class ParamsProjectUpdate extends Params {
	/** id of project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** name */
	@Expose
	private String name;
	/** description */
	@Expose
	private String description;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsProjectUpdate(String projectId) {
		this.projectId = projectId;
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