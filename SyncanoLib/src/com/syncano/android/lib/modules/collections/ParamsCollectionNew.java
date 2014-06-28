package com.syncano.android.lib.modules.collections;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to create new collection
 */

public class ParamsCollectionNew extends Params {
	/** id of specified project */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** collection name */
	@Expose
	private String name;
	/** collection key */
	@Expose
	private String key;
	/** collection description */
	@Expose
	private String description;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param name
	 *            Name for new collection
	 */
	public ParamsCollectionNew(String projectId, String name) {
		this.projectId = projectId;
		this.name = name;
	}

	@Override
	public String getMethodName() {
		return "collection.new";
	}

	public Response instantiateResponse() {
		return new ResponseCollectionNew();
	}

	/**
	 * @return project id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * Sets new collection project id
	 * 
	 * @param project_id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets new collection name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets new collection key
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets new collection description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}