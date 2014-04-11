package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Params;

/**
 * Moves data to folder and/or state.
 * 
 * collection_id/collection_key parameter means that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataMove extends Params {
	/** Project id */
	private String project_id;
	/** Collection id */
	private String collection_id;
	/** Collection key */
	private String collection_key;
	/** Data id array */
	private String[] data_ids;
	/** Folders array */
	private String[] folders;
	/** State */
	private String state;
	/** Filter */
	private String filter;
	/** By user initialized */
	private String by_user;
	/** Limit */
	private Integer limit;
	/** New folder to create */
	private String new_folder;
	/** New data state */
	private String new_state;

	/**
	 * Default constructor
	 * 
	 * @param projectId
	 *            project id
	 * @param collectionId
	 *            collection id
	 * @param collectionKey
	 *            collection key(optional instead collection id)
	 */
	public ParamsDataMove(String projectId, String collectionId, String collectionKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "data.move";
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
	 * 
	 * @return by user
	 */
	public String getBy_user() {
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

	/**
	 * @return new folder
	 */
	public String getNew_folder() {
		return new_folder;
	}

	/**
	 * Sets new folder
	 * 
	 * @param newFolder
	 */
	public void setNewFolder(String newFolder) {
		this.new_folder = newFolder;
	}

	/**
	 * @return new state
	 */
	public String getNewState() {
		return new_state;
	}

	/**
	 * Sets new state
	 * 
	 * @param newState
	 */
	public void setNewState(String newState) {
		this.new_state = newState;
	}

}