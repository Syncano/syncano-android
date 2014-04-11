package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;

/**
 * Params to activate specified collection
 */
public class ParamsCollectionActivate extends Params {
	/** id of specified project */
	private String project_id;
	/** id of specified collection */
	private String collection_id;
	/** force activate */
	private Boolean force;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsCollectionActivate(String projectId, String collectionId) {
		this.project_id = projectId;
		this.collection_id = collectionId;
	}

	@Override
	public String getMethodName() {
		return "collection.activate";
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
	 * @return force activate
	 */
	public Boolean getForce() {
		return force;
	}

	/**
	 * Sets force activate
	 * 
	 * @param force
	 */
	public void setForce(Boolean force) {
		this.force = force;
	}
}