package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents ApiKey object from Syncano Api
 */
public class ApiKey implements Serializable {
	
	private static final long serialVersionUID = 5464907319270907952L;
	/** API client id */
	@Expose
	private String id;
	/**
	 * API client type. Possible values:
	 * <ul>
	 * <li>backend - Backend API client,</li>
	 * <li>user - User API client.</li>
	 * </ul>
	 */
	@Expose
	private String type;
	/** Description of client */
	@Expose
	private String description;
	/** ApiKeys api key */
	@Expose
	@SerializedName(value = "api_key")
	private String apiKey;
	/** ApiKeys role */
	@Expose
	private Role role;

	/**
	 * @return API client id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets API client id
	 * 
	 * @param id
	 *            API client id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return API client type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets API client type
	 * 
	 * @param id
	 *            API client type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return API client description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets API client description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return API key
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * Sets API key
	 * 
	 * @param apiKey
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	/**
	 * @return ApiKeys role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets ApiKeys role
	 * 
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}
}