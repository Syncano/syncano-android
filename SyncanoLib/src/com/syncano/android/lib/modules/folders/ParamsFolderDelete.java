package com.syncano.android.lib.modules.folders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Delete (permanently) specified folder and all associated data. collection_id/collection_key parameter means that one
 * can use either one of them - collection_id or collection_key.
 */
public class ParamsFolderDelete extends Params {
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
	/** folder name */
	@Expose
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