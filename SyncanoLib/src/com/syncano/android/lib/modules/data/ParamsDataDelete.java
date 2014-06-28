package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Deletes Data Object. If no filters are specified, will process all Data Objects in defined collection(s) (up to
 * defined limit). collection_id/collection_key parameter means that one can use either one of them - collection_id or
 * collection_key.
 */
public class ParamsDataDelete extends Params {
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
	/** Data ids array */
	@Expose
	@SerializedName(value = "data_ids")
	private String[] dataIds;
	/** State */
	@Expose
	private String state;
	/** Folders array */
	@Expose
	private String[] folders;
	/** Filter */
	@Expose
	private String filter;
	/** By user */
	@Expose
	@SerializedName(value = "by_user")
	private String byUser;
	/** Limit */
	@Expose
	private Integer limit;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param dataId
	 *            Id of data. Cannot be <code>null</code>.
	 */
	public ParamsDataDelete(String projectId, String collectionId, String collectionKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "data.delete";
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
	 * @return data ids array
	 */
	public String[] getDataIds() {
		return dataIds;
	}

	/**
	 * Sets data ids array
	 * 
	 * @param dataIds
	 */
	public void setDataIds(String[] dataIds) {
		this.dataIds = dataIds;
	}

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return folders array
	 */
	public String[] getFolders() {
		return folders;
	}

	/**
	 * Sets folders array
	 * 
	 * @param folders
	 */
	public void setFolders(String[] folders) {
		this.folders = folders;
	}

	/**
	 * @return filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Sets filter
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * @return by user
	 */
	public String getByUser() {
		return byUser;
	}

	/**
	 * Sets by user
	 * 
	 * @param byUser
	 */
	public void setByUser(String byUser) {
		this.byUser = byUser;
	}

	/**
	 * @return limit
	 */
	public Integer getLimit() {
		return limit;
	}

	/**
	 * Sets limit
	 * 
	 * @param limit
	 */
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}