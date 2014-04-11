package com.syncano.android.lib.modules.folders;

import com.syncano.android.lib.modules.Params;

/**
 * Update existing folder. collection_id/collection_key parameter means that one can use either one of them -
 * collection_id or collection_key.
 */
public class ParamsFolderUpdate extends Params {
	/** project id */
	private String project_id;
	/** collection id (optional) */
	private String collection_id;
	/** collection key(optional) */
	private String collection_key;
	/** name */
	private String name;
	/** new name */
	private String new_name;
	/** source id */
	private String source_id;

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
		return new_name;
	}

	/**
	 * Sets new name
	 * 
	 * @param newName
	 */
	public void setNewName(String newName) {
		this.new_name = newName;
	}

	/**
	 * @return source id
	 */
	public String getSourceId() {
		return source_id;
	}

	/**
	 * Sets source id
	 * 
	 * @param sourceId
	 */
	public void setSourceId(String sourceId) {
		this.source_id = sourceId;
	}

}