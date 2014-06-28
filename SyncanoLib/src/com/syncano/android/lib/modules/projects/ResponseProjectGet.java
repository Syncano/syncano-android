package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Project;

/**
 * Response for get all projects.
 */
public class ResponseProjectGet extends Response {
	/** list of all projects */
	@Expose
	private Project[] project;

	/**
	 * @return projects array
	 */
	public Project[] getProject() {
		return project;
	}

	/**
	 * Sets projects array
	 * 
	 * @param project
	 */
	public void setProject(Project[] project) {
		this.project = project;
	}
}