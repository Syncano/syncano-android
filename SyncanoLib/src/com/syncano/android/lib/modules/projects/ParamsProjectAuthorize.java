package com.syncano.android.lib.modules.projects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Adds container-level permission to specified User API client. Requires Backend API key with Admin permission role.
 */
public class ParamsProjectAuthorize extends Params {
	/** api client id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;
	/** permission */
	@Expose
	private String permission;
	/** project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;

	public ParamsProjectAuthorize(String apiClientId, String permission, String projectId) {
		setApiClientId(apiClientId);
		setPermission(permission);
		setProjectId(projectId);
	}

	@Override
	public String getMethodName() {
		return "project.authorize";
	}

	/**
	 * @return api client id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * @param Sets
	 *            api client id
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

	/**
	 * @return permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param Sets
	 *            permission
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param Sets
	 *            project id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
