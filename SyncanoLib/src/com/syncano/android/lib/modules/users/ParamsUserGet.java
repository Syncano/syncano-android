package com.syncano.android.lib.modules.users;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get users of specified criteria that are associated with Data Objects within specified collection.
 */
public class ParamsUserGet extends Params {
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
	/** State of user */
	@Expose
	private String state;
	/** Foldes arrays */
	@Expose
	private String[] folders;
	/** Custom filter */
	@Expose
	private String filter;

	/**
	 * collection_id/collection_key parameter means that one can use either one of them - collectionId or collectionKey.
	 * 
	 * @param projectId
	 *            Project id. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Collection id. Can be <code>null</code>.
	 * @param collectionKey
	 *            Collection key. Can be <code>null</code>.
	 */
	public ParamsUserGet(String projectId, String collectionId, String collectionKey) {
		this.projectId = projectId;
		this.collectionId = collectionId;
		this.collectionKey = collectionKey;
	}

	@Override
	public String getMethodName() {
		return "user.get";
	}

	public Response instantiateResponse() {
		return new ResponseUserGet();
	}

	/**
	 * @return project id
	 */
	public String getProject_id() {
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
	 * @return state of user
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets state of user
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
	 * @return custom filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Sets custom filter
	 * 
	 * @param filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

}