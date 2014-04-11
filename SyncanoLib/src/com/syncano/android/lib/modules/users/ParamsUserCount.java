package com.syncano.android.lib.modules.users;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to count users with specified criteria.
 */
public class ParamsUserCount extends Params {
	/** id for project */
	private String project_id;
	/** id of collection */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** state of user */
	private String state;
	/** folders */
	private String[] folders;
	/** custom filter */
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
	 * @return collection id from which users will be counted
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
	 * @return collection key from which users will be counted
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