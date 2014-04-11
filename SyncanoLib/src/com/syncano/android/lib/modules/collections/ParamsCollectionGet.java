package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params needed to get all collections
 */
public class ParamsCollectionGet extends Params {
	/** id of specified project */
	private String project_id;
	/** project id */
	private String status;
	/** array of tags */
	private String[] with_tags;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionGet(String projectId) {
		this.project_id = projectId;
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
	public String[] getWith_tags() {
		return with_tags;
	}

	/**
	 * Sets array of tags
	 * 
	 * @param with_tags
	 */
	public void setWith_tags(String[] with_tags) {
		this.with_tags = with_tags;
	}

}