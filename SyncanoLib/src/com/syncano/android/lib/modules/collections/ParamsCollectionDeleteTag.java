package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params to delete selected tags from collection.
 */
public class ParamsCollectionDeleteTag extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** id of specified collection */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** key of specified collection */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** array of tags */
	@Expose
	private String[] tags;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param collectionId
	 *            Id of collection. Cannot be <code>null</code>.
	 * @param tags
	 *            Array of tag items that should be deleted
	 */
	public ParamsCollectionDeleteTag(String projectId, String collectionId, String collectionKey, String[] tags) {
		this.collectionId = collectionId;
        this.collectionKey = collectionKey;
		this.projectId = projectId;
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
	public ParamsCollectionDeleteTag(String projectId, String collectionId, String collectionKey, String tag) {
		this.collectionId = collectionId;
        this.collectionKey = collectionKey;
		this.projectId = projectId;
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
	 * @param collection_key
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
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