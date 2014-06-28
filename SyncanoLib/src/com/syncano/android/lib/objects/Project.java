package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * Represents Project object from Syncano Api
 */
public class Project implements Serializable {
	private static final long serialVersionUID = -4170860989045305407L;
	/** Project id */
	@Expose
	private String id;
	/** Project name */
	@Expose
	private String name;
	/** Project description */
	@Expose
	private String description;

	/**
	 * @return project id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets project id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return project name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets project name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return project description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets project desription
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}