package com.syncano.android.lib.modules.data;

import com.syncano.android.lib.modules.Params;

/**
 * Adds additional parent to data with data_id. If remove_other is True, all other relations for this Data Object will
 * be removed. Note: There can be max 250 parents per Data Object. collection_id/collection_key parameter means that one
 * can use either one of them - collection_id or collection_key.
 */
public class ParamsDataAddParent extends Params {
	/** project id */
	private String project_id;
	/** collection id */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** data id */
	private String data_id;
	/** parent id */
	private String parent_id;
	/** remove other switch */
	private Boolean remove_other;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param dataId
	 *            Id of data. Cannot be <code>null</code>.
	 */
	public ParamsDataAddParent(String projectId, String collectionId, String collectionKey, String dataId,
			String parentId) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setDataId(dataId);
		setParentId(parentId);
	}

	@Override
	public String getMethodName() {
		return "data.add_parent";
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
	 * @return data id
	 */
	public String getDataId() {
		return data_id;
	}

	/**
	 * Sets data id
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.data_id = dataId;
	}

	/**
	 * @return parent id
	 */
	public String getParentId() {
		return parent_id;
	}

	/**
	 * Sets parent id
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parent_id = parentId;
	}

	/**
	 * @return remove other
	 */
	public Boolean getRemoveOther() {
		return remove_other;
	}

	/**
	 * Sets remove other
	 * 
	 * @param removeOther
	 */
	public void setRemoveOther(Boolean removeOther) {
		this.remove_other = removeOther;
	}

}