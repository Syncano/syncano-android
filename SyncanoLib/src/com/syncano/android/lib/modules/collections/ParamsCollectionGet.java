package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params needed to get all collections
 */
public class ParamsCollectionGet extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** project id */
	@Expose
	private String status;
	/** array of tags */
	@Expose
	@SerializedName(value = "with_tags")
	private String[] withTags;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionGet(String projectId) {
		this.projectId = projectId;
	}

	@Override
	public String getMethodName() {
		return "collection.get";
	}

	public Response instantiateResponse() {
		return new ResponseCollectionGet();
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
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return array of tags
	 */
	public String[] getWithTags() {
		return withTags;
	}

	/**
	 * Sets array of tags
	 * 
	 * @param with_tags
	 */
	public void setWithTags(String[] withTags) {
		this.withTags = withTags;
	}

}