package com.syncano.android.lib.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Collections object from Syncano Api
 */
public class Collection implements Serializable {
	private static final long serialVersionUID = -7038599495663010560L;
	/** collection id */
	@Expose
	private String id;
	/** collection status */
	@Expose
	private String status;
	/** name */
	@Expose
	private String name;
	/** description */
	@Expose
	private String description;
	/** collection key */
	@Expose
	private String key;
	/** start date */
	@Expose
	@SerializedName(value = "start_date")
	private Date startDate;
	/** end date */
	@Expose
	@SerializedName(value = "end_date")
	private Date endDate;
	/** additional tags map */
	@Expose
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
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets start date
	 * 
	 * @param start_date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return end date
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Sets end date
	 * 
	 * @param end_date
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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