package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Adds container-level permission to specified User API client. Requires Backend API key with Admin permission role.
 * 
 * The collection_id/collection_key parameter means that one can use either one of them - collection_id or
 * collection_key.
 */
public class ParamsCollectionAuthorize extends Params {
	/** api client id */
	@Expose
	@SerializedName(value = "api_client_id")
	private String apiClientId;
	/** permission */
	@Expose
	private String permission;
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

	public ParamsCollectionAuthorize(String apiClientId, String permission, String projectId, String collectionId,
			String collectionKey) {
		setApiClientId(apiClientId);
		setPermission(permission);
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "collection.authorize";
	}

	/**
	 * @return api client id
	 */
	public String getApiClientId() {
		return apiClientId;
	}

	/**
	 * @param Sets
	 *            api client id
	 */
	public void setApiClientId(String apiClientId) {
		this.apiClientId = apiClientId;
	}

	/**
	 * @return permission
	 */
	public String getPermission() {
		return permission;
	}

	/**
	 * @param Sets
	 *            permission
	 */
	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * @return projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param Sets
	 *            project id
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
	 * @param Sets
	 *            collection id
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
	 * @param Sets
	 *            collection key
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}
}
