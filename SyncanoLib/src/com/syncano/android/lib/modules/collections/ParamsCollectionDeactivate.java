package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to deactivate specified collection
 */

public class ParamsCollectionDeactivate extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** id of specified collection */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** key of specified collection */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;

	/**
	 * 
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionDeactivate(String projectId, String collectionId) {
		this.projectId = projectId;
		this.collectionId = collectionId;
	}

	@Override
	public String getMethodName() {
		return "collection.deactivate";
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
	 * @param projectId
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
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * @return collection key
	 */
	public String getCollectionKey() {
		return collectionKey;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collectionKey
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

}