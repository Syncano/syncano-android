package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to get one collection
 */
public class ParamsCollectionGetOne extends Params {
	/** id of specified project */
	private String project_id;
	/** id of specified collection */
	private String collection_id;
	/** key of specified collection */
	private String collection_key;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionGetOne(String projectId, String collectionId) {
		this.project_id = projectId;
		this.collection_id = collectionId;
	}

	@Override
	public String getMethodName() {
		return "collection.get_one";
	}

	public Response instantiateResponse() {
		return new ResponseCollectionGetOne();
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

}