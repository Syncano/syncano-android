package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;

/**
 * Params to delete selected tags from collection.
 */
public class ParamsCollectionDeleteTag extends Params {
	/** id of specified project */
	private String project_id;
	/** id of specified collection */
	private String collection_id;
	/** key of specified collection */
	private String collection_key;
	/** array of tags */
	private String[] tags;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param tags
	 *            Array of tag items that should be deleted
	 */
	public ParamsCollectionDeleteTag(String projectId, String collectionId, String[] tags) {
		this.collection_id = collectionId;
		this.project_id = projectId;
		this.tags = tags;
	}

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param tag
	 *            Tag item that should be deleted
	 */
	public ParamsCollectionDeleteTag(String projectId, String collectionId, String tag) {
		this.collection_id = collectionId;
		this.project_id = projectId;
		String[] tags = { tag };
		this.tags = tags;
	}

	@Override
	public String getMethodName() {
		return "collection.delete_tag";
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
	public void setCollection_id(String collectionId) {
		this.collection_id = collectionId;
	}

	/**
	 * @return collection key
	 */
	public String getCollection_key() {
		return collection_key;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collection_key
	 */
	public void setCollection_key(String collection_key) {
		this.collection_key = collection_key;
	}

	/**
	 * @return array of tags
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Sets array of tags
	 * 
	 * @param tags
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

}