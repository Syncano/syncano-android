package com.syncano.android.lib.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * Represents Collections object from Syncano Api
 */
public class Collection implements Serializable {
	private static final long serialVersionUID = -7038599495663010560L;
	/** collection id */
	private String id;
	/** collection status */
	private String status;
	/** name */
	private String name;
	/** description */
	private String description;
	/** collection key */
	private String key;
	/** start date */
	private Date start_date;
	/** end date */
	private Date end_date;
	/** additional tags map */
	private HashMap<String, String> tags;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets status
	 * 
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name
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
	 * Sets key
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return start date
	 */
	public Date getStart_date() {
		return start_date;
	}

	/**
	 * Sets start date
	 * 
	 * @param start_date
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	/**
	 * @return end date
	 */
	public Date getEnd_date() {
		return end_date;
	}

	/**
	 * Sets end date
	 * 
	 * @param end_date
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	/**
	 * @return tags map
	 */
	public HashMap<String, String> getTags() {
		return tags;
	}

	/**
	 * Sets tags map
	 * 
	 * @param tags
	 */
	public void setTags(HashMap<String, String> tags) {
		this.tags = tags;
	}

	/**
	 * @return collection description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets collection description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}