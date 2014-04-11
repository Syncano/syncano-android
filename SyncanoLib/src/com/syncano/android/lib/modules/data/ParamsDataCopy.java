package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Copies data with data_id. Copy has data_key cleared. collection_id/collection_key parameter means that one can use
 * either one of them - collection_id or collection_key.
 */
public class ParamsDataCopy extends Params {
	/** Project id */
	private String project_id;
	/** Collection id */
	private String collection_id;
	/** Collection key */
	private String collection_key;
	/** Data ids array */
	private String[] data_ids;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param dataIds
	 *            Data id or ids.. Cannot be <code>null</code>.
	 */
	public ParamsDataCopy(String projectId, String collectionId, String collectionKey, String[] dataIds) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setDataIds(dataIds);
	}

	@Override
	public String getMethodName() {
		return "data.copy";
	}

	public Response instantiateResponse() {
		return new ResponseDataCopy();
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
}