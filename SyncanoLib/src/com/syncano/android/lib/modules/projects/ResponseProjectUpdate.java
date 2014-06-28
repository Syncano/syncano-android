package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Project;

/**
 * Response for update existing project.
 */
public class ResponseProjectUpdate extends Response {
	@Expose
	private Project project;

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
}