package com.syncano.android.lib.objects;

import java.io.Serializable;

/**
 * Represents Folder object from Syncano Api
 */
public class Folder implements Serializable {
	private static final long serialVersionUID = 9209121215937599337L;
	/** id of folder */
	private String id;
	/** informs that folder is custom */
	private Boolean is_custom;
	/** folder name */
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
		return is_custom;
	}

	/**
	 * Sets information if folder is custom
	 * 
	 * @param isCustom
	 */
	public void setIsCustom(Boolean isCustom) {
		this.is_custom = isCustom;
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