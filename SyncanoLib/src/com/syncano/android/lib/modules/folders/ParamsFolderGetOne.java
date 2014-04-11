package com.syncano.android.lib.modules.folders;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Get folders for specified collection. collection_id/collection_key parameter means that one can use either one of
 * them - collection_id or collection_key.
 */
public class ParamsFolderGetOne extends Params {
	/** project id */
	private String project_id;
	/** collection id */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** folder name */
	private String folder_name;

	/**
	 * @param projectId
	 *            Project id.
	 * @param collectionId
	 *            Collection id or key defining collection for which folder will be returned.
	 * @param collectionKey
	 *            Collection id or key defining collection for which folder will be returned.
	 * @param folderName
	 *            Folder name defining folder.
	 */
	public ParamsFolderGetOne(String projectId, String collectionId, String collectionKey, String folderName) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setFolderName(folderName);
	}

	@Override
	public String getMethodName() {
		return "folder.get_one";
	}

	public Response instantiateResponse() {
		return new ResponseFolderNew();
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
	public String getFolderName() {
		return folder_name;
	}

	/**
	 * Sets folder name
	 * 
	 * @param folderName
	 */
	public void setFolderName(String folderName) {
		this.folder_name = folderName;
	}
}