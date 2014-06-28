package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Params;

/**
 * Adds additional child to data with specified data_id. If remove_other is True, all other children of specified Data
 * Object will be removed.
 * 
 * The collection_id/collection_key parameter means that one can use either one of them - collection_id or
 * collection_key.
 */
public class ParamsDataAddChild extends Params {
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
	/** child id */
	@Expose
	@SerializedName(value = "child_id")
	private String childId;
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
	 * @param childId
	 *            Id of child.
	 */
	public ParamsDataAddChild(String projectId, String collectionId, String collectionKey, String dataId, String childId) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setDataId(dataId);
		setChildId(childId);
	}

	@Override
	public String getMethodName() {
		return "data.add_child";
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
	 * @return child id
	 */
	public String getChildId() {
		return childId;
	}

	/**
	 * Sets child id
	 * 
	 * @param childId
	 */
	public void setChildId(String childId) {
		this.childId = childId;
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