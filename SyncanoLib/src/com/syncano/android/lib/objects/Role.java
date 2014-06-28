package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * Represents Role object from Syncano Api
 */
public class Role implements Serializable {

	private static final long serialVersionUID = 3602260716616995635L;
	/** Role id */
	@Expose
	private String id;
	/** Role name */
	@Expose
	private String name;

	/**
	 * @return role id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets role id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return role name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets role name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}