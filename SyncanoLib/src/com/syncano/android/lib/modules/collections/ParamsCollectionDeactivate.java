package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;

/**
 * Params to deactivate specified collection
 */

public class ParamsCollectionDeactivate extends Params {
	/** id of specified project */
	private String project_id;
	/** id of specified collection */
	private String collection_id;
	/** key of specified collection */
	private String collection_key;

	/**
	 * 
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionDeactivate(String projectId, String collectionId) {
		this.project_id = projectId;
		this.collection_id = collectionId;
	}

	@Override
	public String getMethodName() {
		return "collection.deactivate";
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
	public void setCollection_id(String collectionId) {
		this.collection_id = collectionId;
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
	 * @param collectionKey
	 */
	public void setCollection_key(String collectionKey) {
		this.collection_key = collectionKey;
	}

}