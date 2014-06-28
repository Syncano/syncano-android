package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Params;

/**
 * Adds additional parent to data with data_id. If remove_other is True, all other relations for this Data Object will
 * be removed. Note: There can be max 250 parents per Data Object. collection_id/collection_key parameter means that one
 * can use either one of them - collection_id or collection_key.
 */
public class ParamsDataAddParent extends Params {
	/** project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** collection id */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** collection key */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** data id */
	@Expose
	@SerializedName(value = "data_id")
	private String dataId;
	/** parent id */
	@Expose
	@SerializedName(value = "parent_id")
	private String parentId;
	/** remove other switch */
	@Expose
	@SerializedName(value = "remove_other")
	private Boolean removeOther;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param dataId
	 *            Id of data. Cannot be <code>null</code>.
	 */
	public ParamsDataAddParent(String projectId, String collectionId, String collectionKey, String dataId,
			String parentId) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setDataId(dataId);
		setParentId(parentId);
	}

	@Override
	public String getMethodName() {
		return "data.add_parent";
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

	/**
	 * @return remove other
	 */
	public Boolean getRemoveOther() {
		return removeOther;
	}

	/**
	 * Sets remove other
	 * 
	 * @param removeOther
	 */
	public void setRemoveOther(Boolean removeOther) {
		this.removeOther = removeOther;
	}

}