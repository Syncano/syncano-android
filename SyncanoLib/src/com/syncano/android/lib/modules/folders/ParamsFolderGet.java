package com.syncano.android.lib.modules.folders;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get folders for specified collection. collection_id/collection_key parameter means that one can use either one of
 * them - collection_id or collection_key.
 */
public class ParamsFolderGet extends Params {
	/** project id */
	private String project_id;
	/** Collection id defining collection for which folders will be returned. */
	private String collection_id;
	/** Collection key defining collection for which folders will be returned. */
	private String collection_key;

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

}