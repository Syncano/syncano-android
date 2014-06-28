package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to count users with specified criteria.
 */
public class ParamsUserCount extends Params {
	/** id for project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** id of collection */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** collection key */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** state of user */
	@Expose
	private String state;
	/** folders */
	@Expose
	private String[] folders;
	/** custom filter */
	@Expose
	private String filter;

	@Override
	public String getMethodName() {
		return "user.count";
	}

	public Response instantiateResponse() {
		return new ResponseUserCount();
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
	 * @return collection id from which users will be counted
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
	 * @return collection key from which users will be counted
	 */
	public String getCollectionKey() {
		return collectionKey;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collection_key
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	/**
	 * @return State for counted users
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
	 * @return array of folders
	 */
	public String[] getFolders() {
		return folders;
	}

	/**
	 * Sets array of folders
	 * 
	 * @param folders
	 */
	public void setFolders(String[] folders) {
		this.folders = folders;
	}

	/**
	 * @return Custom filters
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Sets custom filters
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

}