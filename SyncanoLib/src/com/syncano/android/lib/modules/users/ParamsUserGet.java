package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get users of specified criteria that are associated with Data Objects within specified collection.
 */
public class ParamsUserGet extends Params {
	/** Project id */
	private String project_id;
	/** Collection id */
	private String collection_id;
	/** Collection key */
	private String collection_key;
	/** State of user */
	private String state;
	/** Foldes arrays */
	private String[] folders;
	/** Custom filter */
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
		this.project_id = projectId;
		this.collection_id = collectionId;
		this.collection_key = collectionKey;
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
		return project_id;
	}

	/**
	 * Sets project id
	 * 
	 * @param project_id
	 */
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}

	/**
	 * @return collection id
	 */
	public String getCollection_id() {
		return collection_id;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collection_id
	 */
	public void setCollection_id(String collection_id) {
		this.collection_id = collection_id;
	}

	/**
	 * @return collection key
	 */
	public String getCollection_key() {
		return collection_key;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collection_key
	 */
	public void setCollection_key(String collection_key) {
		this.collection_key = collection_key;
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