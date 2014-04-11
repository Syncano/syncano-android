package com.syncano.android.lib.modules.collections;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Params to create new collection
 */

public class ParamsCollectionNew extends Params {
	/** id of specified project */
	private String project_id;
	/** collection name */
	private String name;
	/** collection key */
	private String key;
	/** collection description */
	private String description;

	/**
	 * @param projectId
	 *            Id of project in which exists collection. Cannot be <code>null</code>.
	 * @param name
	 *            Name for new collection
	 */
	public ParamsCollectionNew(String projectId, String name) {
		this.project_id = projectId;
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
	public String getProject_id() {
		return project_id;
	}

	/**
	 * Sets new collection project id
	 * 
	 * @param project_id
	 */
	public void setProject_id(String project_id) {
		this.project_id = project_id;
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