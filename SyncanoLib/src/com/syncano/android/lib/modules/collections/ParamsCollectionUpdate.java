package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

public class ParamsCollectionUpdate extends Params {
	/** id of specified project */
	private String project_id;
	/** id of specified collection */
	private String collection_id;
	/** key of specified collection */
	private String collection_key;
	/** collection new name */
	private String name;
	/** collection new description */
	private String description;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionUpdate(String projectId, String collectionId) {
		this.project_id = projectId;
		this.collection_id = collectionId;
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
	public String getProject_id() {
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
	public void setCollectionId(String collectionId) {
		this.collection_id = collectionId;
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