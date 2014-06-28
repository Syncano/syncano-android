package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to activate specified collection
 */
public class ParamsCollectionActivate extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** id of specified collection */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** force activate */
	@Expose
	private Boolean force;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionActivate(String projectId, String collectionId) {
		this.projectId = projectId;
		this.collectionId = collectionId;
	}

	@Override
	public String getMethodName() {
		return "collection.activate";
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
	 * @return collection id
	 */
	public String getCollectionId() {
		return collectionId;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collection_id
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * @return force activate
	 */
	public Boolean getForce() {
		return force;
	}

	/**
	 * Sets force activate
	 * 
	 * @param force
	 */
	public void setForce(Boolean force) {
		this.force = force;
	}
}