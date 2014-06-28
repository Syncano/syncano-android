package com.syncano.android.lib.modules.subscriptions;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to subscribe to collection within specified project.
 */
public class ParamsSubscriptionSubscribeCollection extends Params {
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
	/** Context to subscribe within */
	@Expose
	private String context;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 */
	public ParamsSubscriptionSubscribeCollection(String projectId, String collectionId) {
		this.projectId = projectId;
		this.collectionId = collectionId;
	}

	@Override
	public String getMethodName() {
		return "subscription.subscribe_collection";
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
	 * @param project_id
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
	 * @param collection_id
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * @return key for specified collection
	 */
	public String getCollectionKey() {
		return collectionKey;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collection_key
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	/**
	 * Sets context
	 * 
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	/**
	 * @return context
	 */
	public String getContext() {
		return context;
	}

}