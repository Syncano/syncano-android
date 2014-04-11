package com.syncano.android.lib.objects;

import java.io.Serializable;

/**
 * Represents Identity object from Syncano Api
 */
public class Identity implements Serializable {
	private static final long serialVersionUID = 4213280469759255245L;
	/** identity id */
	private String id;
	/** client id */
	private String client_id;
	/** uuid */
	private String uuid;
	/** name */
	private String name;
	/** state */
	private String state;
	/** source */
	private String source;

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
	 * @return client id
	 */
	public String getClient_id() {
		return client_id;
	}

	/**
	 * Sets client id
	 * 
	 * @param client_id
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}

	/**
	 * @return uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Sets uuid
	 * 
	 * @param uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets state
	 * 
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets source
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return identity name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets identity name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}