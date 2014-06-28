package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Moves data to folder and/or state.
 * 
 * collection_id/collection_key parameter means that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataMove extends Params {
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
	/** Data id array */
	@Expose
	@SerializedName(value = "data_ids")
	private String[] dataIds;
	/** Folders array */
	@Expose
	private String[] folders;
	/** State */
	@Expose
	private String state;
	/** Filter */
	@Expose
	private String filter;
	/** By user initialized */
	@Expose
	@SerializedName(value = "by_user")
	private String byUser;
	/** Limit */
	@Expose
	private Integer limit;
	/** New folder to create */
	@Expose
	@SerializedName(value = "new_folder")
	private String newFolder;
	/** New data state */
	@Expose
	@SerializedName(value = "new_state")
	private String newState;

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

	/**
	 * @return new folder
	 */
	public String getNewFolder() {
		return newFolder;
	}

	/**
	 * Sets new folder
	 * 
	 * @param newFolder
	 */
	public void setNewFolder(String newFolder) {
		this.newFolder = newFolder;
	}

	/**
	 * @return new state
	 */
	public String getNewState() {
		return newState;
	}

	/**
	 * Sets new state
	 * 
	 * @param newState
	 */
	public void setNewState(String newState) {
		this.newState = newState;
	}

}