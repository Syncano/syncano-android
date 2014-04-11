package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;

/**
 * Params to delete specified collection
 */
public class ParamsCollectionDelete extends Params {
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
	public ParamsCollectionDelete(String projectId, String collectionId) {
		this.collection_id = collectionId;
		this.project_id = projectId;
	}

	@Override
	public String getMethodName() {
		return "collection.delete";
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
	public void setProject_id(String projectId) {
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
	public String getCollectionKey() {
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