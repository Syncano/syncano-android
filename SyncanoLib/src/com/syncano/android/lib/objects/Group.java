package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Group object from Syncano Api
 */
public class Group implements Serializable {
	private static final long serialVersionUID = -8008454880647420245L;
	/** Group id */
	@Expose
	private Integer id;
	/** Group id in string format */
	@Expose
	@SerializedName(value = "id_str")
	private String idStr;
	/** Group name */
	@Expose
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
	public String getIdStr() {
		return idStr;
	}

	/**
	 * Sets group id string
	 * 
	 * @param id_str
	 */
	public void setIdStr(String idStr) {
		this.idStr = idStr;
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