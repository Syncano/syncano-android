package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Params;

/**
 * Deletes Data Object. If no filters are specified, will process all Data Objects in defined collection(s) (up to
 * defined limit). collection_id/collection_key parameter means that one can use either one of them - collection_id or
 * collection_key.
 */
public class ParamsDataDelete extends Params {
	/** Project id */
	private String project_id;
	/** Collection id */
	private String collection_id;
	/** Collection key */
	private String collection_key;
	/** Data ids array */
	private String[] data_ids;
	/** State */
	private String state;
	/** Folders array */
	private String[] folders;
	/** Filter */
	private String filter;
	/** By user */
	private String by_user;
	/** Limit */
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
		return project_id;
	}

	/**
	 * Sets project id
	 * 
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.project_id = projectId;
	}

	/**
	 * @return collection id
	 */
	public String getCollectionId() {
		return collection_id;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collection_id = collectionId;
	}

	/**
	 * @return collection key
	 */
	public String getCollectionKey() {
		return collection_key;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collectionKey
	 */
	public void setCollectionKey(String collectionKey) {
		this.collection_key = collectionKey;
	}

	/**
	 * @return data ids array
	 */
	public String[] getDataIds() {
		return data_ids;
	}

	/**
	 * Sets data ids array
	 * 
	 * @param dataIds
	 */
	public void setDataIds(String[] dataIds) {
		this.data_ids = dataIds;
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
		return by_user;
	}

	/**
	 * Sets by user
	 * 
	 * @param byUser
	 */
	public void setByUser(String byUser) {
		this.by_user = byUser;
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