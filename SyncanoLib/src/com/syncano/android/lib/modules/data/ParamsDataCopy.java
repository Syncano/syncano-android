package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Copies data with data_id. Copy has data_key cleared. collection_id/collection_key parameter means that one can use
 * either one of them - collection_id or collection_key.
 */
public class ParamsDataCopy extends Params {
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
	/** Data ids array */
	@Expose
	@SerializedName(value = "data_ids")
	private String[] dataIds;

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
	 * @return data ids array
	 */
	public String[] getDataIds() {
		return dataIds;
	}

	/**
	 * Sets data ids array
	 * 
	 * @param dataIds
	 */
	public void setDataIds(String[] dataIds) {
		this.dataIds = dataIds;
	}
}