package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get data by data_id or data_key. Either data_id or data_key has to be specified. collection_id/collection_key
 * parameter means that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataGetOne extends Params {
	/** project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** collection id */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** collection key */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** data id */
	@Expose
	@SerializedName(value = "data_id")
	private String dataId;
	/** data key */
	@Expose
	@SerializedName(value = "data_key")
	private String dataKey;
	/** include children */
	@Expose
	@SerializedName(value = "include_children")
	private Boolean includeChildren;
	/** depth */
	@Expose
	private Integer depth;
	/** children limit */
	@Expose
	@SerializedName(value = "children_limit")
	private Integer childrenLimit;

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
	 * @return data id
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * Sets data id
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * @return data key
	 */
	public String getDataKey() {
		return dataKey;
	}

	/**
	 * Sets data key
	 * 
	 * @param dataKey
	 */
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	/**
	 * @return include children
	 */
	public Boolean getIncludeChildren() {
		return includeChildren;
	}

	/**
	 * @param Sets include include children
	 */
	public void setIncludeChildren(Boolean includeChildren) {
		this.includeChildren = includeChildren;
	}

	/**
	 * @return depth
	 */
	public Integer getDepth() {
		return depth;
	}

	/**
	 * @param Sets depth
	 */
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	/**
	 * @return childrenLimit
	 */
	public Integer getChildrenLimit() {
		return childrenLimit;
	}

	/**
	 * @param Sets children limit
	 */
	public void setChildrenLimit(Integer childrenLimit) {
		this.childrenLimit = childrenLimit;
	}
	
}