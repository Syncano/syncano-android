package com.syncano.android.lib.objects;

import java.io.Serializable;

/**
 * Represents Group object from Syncano Api
 */
public class Group implements Serializable {
	private static final long serialVersionUID = -8008454880647420245L;
	/** Group id */
	private Integer id;
	/** Group id in string format */
	private String id_str;
	/** Group name */
	private String name;

	/**
	 * @return group id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets group id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return group id in string
	 */
	public String getId_str() {
		return id_str;
	}

	/**
	 * Sets group id string
	 * 
	 * @param id_str
	 */
	public void setId_str(String id_str) {
		this.id_str = id_str;
	}

	/**
	 * @return group name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets group name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

}