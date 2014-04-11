package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Counts data of specified criteria. collection_id/collection_key parameter means that one can use either one of them -
 * collection_id or collection_key.
 */
public class ParamsDataCount extends Params {
	/** Project id */
	private String project_id;
	/** Collection id */
	private String collection_id;
	/** Collection key */
	private String collection_key;
	/** State */
	private String state;
	/** Folders array */
	private String[] folders;
	/** Filter */
	private String filter;
	/** by user */
	private String by_user;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsDataCount(String projectId, String collectionId, String collectionKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "data.count";
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseDataCount();
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
	 * 
	 * @return by_user
	 */
	public String getByUser() {
		return by_user;
	}

	/**
	 * Sets "by user"
	 * 
	 * @param byUser
	 */
	public void setByUser(String byUser) {
		this.by_user = byUser;
	}

}