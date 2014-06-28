package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Removes a parent from data with data_id. collection_id/collection_key parameter means that one can use either one of
 * them - collection_id or collection_key.
 */
public class ParamsDataRemoveParent extends Params {
	/** Project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** Collection id */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** Collection key */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** Data id */
	@Expose
	@SerializedName(value = "data_id")
	private String dataId;
	/** Parent id */
	@Expose
	@SerializedName(value = "parent_id")
	private String parentId;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param dataId
	 *            Id of data. Cannot be <code>null</code>.
	 */
	public ParamsDataRemoveParent(String projectId, String collectionId, String collectionKey, String dataId) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setDataId(dataId);
	}

	@Override
	public String getMethodName() {
		return "data.remove_parent";
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

	/**
	 * @return data id
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * Sets data id
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * @return parent id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets parent id
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}
