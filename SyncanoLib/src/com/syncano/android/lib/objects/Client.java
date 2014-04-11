package com.syncano.android.lib.objects;

import java.io.Serializable;

/**
 * Represents Client object from Syncano Api
 */
public class Client implements Serializable {

	private static final long serialVersionUID = 5464907319270907952L;
	/** Id of client */
	private String id;
	/** Description of client */
	private String description;
	/** Clients api key */
	private String api_key;
	/** Clients role */
	private Role role;

	/**
	 * @return id of client
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets clients id
	 * 
	 * @param id
	 *            client id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return client description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return clients api key
	 */
	public String getApiKey() {
		return api_key;
	}

	/**
	 * Sets client api key
	 * 
	 * @param apiKey
	 */
	public void setApiKey(String apiKey) {
		this.api_key = apiKey;
	}

	/**
	 * @return clients role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets client role
	 * 
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}
}