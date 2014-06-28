package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

public class ParamsCollectionUpdate extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** id of specified collection */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** key of specified collection */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** collection new name */
	@Expose
	private String name;
	/** collection new description */
	@Expose
	private String description;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionUpdate(String projectId, String collectionId) {
		this.projectId = projectId;
		this.collectionId = collectionId;
	}

	@Override
	public String getMethodName() {
		return "collection.update";
	}

	public Response instantiateResponse() {
		return new ResponseCollectionUpdate();
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
	 * @return collection name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets collection new name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return new description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets new description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}