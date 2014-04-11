package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;

/**
 * Params needed to add tag to specified collection
 */

public class ParamsCollectionAddTag extends Params {
	/** project id */
	private String project_id;
	/** collection id */
	private String collection_id;
	/** collection key */
	private String collection_key;
	/** tags array */
	private String[] tags;
	/** weight */
	private Float weight;
	/** remove other option */
	private Boolean remove_other;

	/**
	 * 
	 * @param projectId
	 *            project to add tag
	 */
	public ParamsCollectionAddTag(String projectId) {
		setProjectId(projectId);
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
	public Boolean getRemove_other() {
		return remove_other;
	}

	/**
	 * Sets remove other boolean
	 * 
	 * @param remove_other
	 */
	public void setRemove_other(Boolean remove_other) {
		this.remove_other = remove_other;
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
}