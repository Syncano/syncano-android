package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Params needed to add tag to specified collection
 */

public class ParamsCollectionAddTag extends Params {
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
	/** tags array */
	@Expose
	private String[] tags;
	/** weight */
	@Expose
	private Float weight;
	/** remove other option */
	@Expose
	@SerializedName(value = "remove_other")
	private Boolean removeOther;

	/**
	 * 
	 * @param projectId
	 *            project to add tag
	 */
	public ParamsCollectionAddTag(String projectId, String collectionId, String collectionKey, String[] tags) {
		setProjectId(projectId);
        setCollectionId(collectionId);
        setCollectionKey(collectionKey);
        setTags(tags);
	}

	@Override
	public String getMethodName() {
		return "collection.add_tag";
	}

	/**
	 * @return tags array
	 */
	public String[] getTags() {
		return tags;
	}

	/**
	 * Sets tags array
	 * 
	 * @param tags
	 */
	public void setTags(String[] tags) {
		this.tags = tags;
	}

	/**
	 * @return weight
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * Sets weight
	 * 
	 * @param weight
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * @return remove other
	 */
	public Boolean getRemoveOther() {
		return removeOther;
	}

	/**
	 * Sets remove other boolean
	 * 
	 * @param remove_other
	 */
	public void setRemoveOther(Boolean removeOther) {
		this.removeOther = removeOther;
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
}