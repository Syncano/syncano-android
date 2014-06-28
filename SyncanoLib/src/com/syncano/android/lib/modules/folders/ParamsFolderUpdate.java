package com.syncano.android.lib.modules.folders;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Update existing folder. collection_id/collection_key parameter means that one can use either one of them -
 * collection_id or collection_key.
 */
public class ParamsFolderUpdate extends Params {
	/** project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** collection id (optional) */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** collection key(optional) */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** name */
	@Expose
	private String name;
	/** new name */
	@Expose
	@SerializedName(value = "new_name")
	private String newName;
	/** source id */
	@Expose
	@SerializedName(value = "source_id")
	private String sourceId;

	/**
	 * @param projectId
	 *            Project id.
	 * @param collectionId
	 *            Collection id or key defining collection where folder exists.
	 * @param collectionKey
	 *            Collection id or key defining collection where folder exists.
	 * @param name
	 *            Current folder name.
	 */
	public ParamsFolderUpdate(String projectId, String collectionId, String collectionKey, String name) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setName(name);
	}

	@Override
	public String getMethodName() {
		return "folder.update";
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
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return new name
	 */
	public String getNewName() {
		return newName;
	}

	/**
	 * Sets new name
	 * 
	 * @param newName
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * @return source id
	 */
	public String getSourceId() {
		return sourceId;
	}

	/**
	 * Sets source id
	 * 
	 * @param sourceId
	 */
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

}