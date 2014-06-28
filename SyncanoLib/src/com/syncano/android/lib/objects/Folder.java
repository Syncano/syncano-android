package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Folder object from Syncano Api
 */
public class Folder implements Serializable {
	private static final long serialVersionUID = 9209121215937599337L;
	/** id of folder */
	@Expose
	private String id;
	/** informs that folder is custom */
	@Expose
	@SerializedName(value = "is_custom")
	private Boolean isCustom;
	/** folder name */
	@Expose
	private String name;

	/**
	 * @return folders id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets folders id
	 * 
	 * @param id
	 *            folders id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Defines if folder is custom
	 * 
	 * @return folder is custom
	 */
	public Boolean getIsCustom() {
		return isCustom;
	}

	/**
	 * Sets information if folder is custom
	 * 
	 * @param isCustom
	 */
	public void setIsCustom(Boolean isCustom) {
		this.isCustom = isCustom;
	}

	/**
	 * @return folder name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets folder name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}