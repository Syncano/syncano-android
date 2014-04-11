package com.syncano.android.lib.modules.folders;

import com.syncano.android.lib.modules.Params;

/**
 * Delete (permanently) specified folder and all associated data. collection_id/collection_key parameter means that one
 * can use either one of them - collection_id or collection_key.
 */
public class ParamsFolderDelete extends Params {
	/** project id */
	private String project_id;
	/** collection id */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** folder name */
	private String name;

	/**
	 * @param projectId
	 *            Project id.
	 * @param collectionId
	 *            Collection id or key defining collection where folder exists.
	 * @param collectionKey
	 *            Collection id or key defining collection where folder exists.
	 * @param name
	 *            Folder name to delete.
	 */
	public ParamsFolderDelete(String projectId, String collectionId, String collectionKey, String name) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setName(name);
	}

	@Override
	public String getMethodName() {
		return "folder.delete";
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
	 * @return folder name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets folder name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}