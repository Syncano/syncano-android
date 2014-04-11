package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get data by data_id or data_key. Either data_id or data_key has to be specified. collection_id/collection_key
 * parameter means that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataGetOne extends Params {
	/** project id */
	private String project_id;
	/** collection id */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** data id */
	private String data_id;
	/** data key */
	private String data_key;

	public ParamsDataGetOne(String projectId, String collectionId, String collectionKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "data.get_one";
	}

	public Response instantiateResponse() {
		return new ResponseDataGetOne();
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
	 * @return data id
	 */
	public String getDataId() {
		return data_id;
	}

	/**
	 * Sets data id
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.data_id = dataId;
	}

	/**
	 * @return data key
	 */
	public String getDataKey() {
		return data_key;
	}

	/**
	 * Sets data key
	 * 
	 * @param dataKey
	 */
	public void setDataKey(String dataKey) {
		this.data_key = dataKey;
	}

}