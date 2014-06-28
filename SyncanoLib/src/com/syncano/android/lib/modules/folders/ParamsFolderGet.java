package com.syncano.android.lib.modules.folders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get folders for specified collection. collection_id/collection_key parameter means that one can use either one of
 * them - collection_id or collection_key.
 */
public class ParamsFolderGet extends Params {
	/** project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** Collection id defining collection for which folders will be returned. */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** Collection key defining collection for which folders will be returned. */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;

	/**
	 * @param projectId
	 *            Project id.
	 * @param collectionId
	 *            Collection id or key defining collection for which folders will be returned.
	 * @param collectionKey
	 *            Collection id or key defining collection for which folders will be returned.
	 */
	public ParamsFolderGet(String projectId, String collectionId, String collectionKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
	}

	@Override
	public String getMethodName() {
		return "folder.get";
	}

	public Response instantiateResponse() {
		return new ResponseFolderGet();
	}

	/**
	 * @return project id
	 */
	public String getProject_id() {
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

}