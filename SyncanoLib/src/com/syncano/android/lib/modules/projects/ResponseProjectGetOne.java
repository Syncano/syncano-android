package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Project;

/**
 * Response for get one project.
 */
public class ResponseProjectGetOne extends Response {
	/** project to get */
	@Expose
	private Project project;

	/**
	 * Sets project
	 * 
	 * @param project
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @return project
	 */
	public Project getProject() {
		return project;
	}
}