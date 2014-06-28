package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

/**
 * Class representing user from Syncano API
 */
public class User implements Serializable {
	private static final long serialVersionUID = 76320719703288514L;
	/** users id */
	@Expose
	private String id;
	/** name */
	@Expose
	private String name;
	/** nickname */
	@Expose
	private String nick;
	/** avatar url */
	@Expose
	private Avatar avatar;

	/**
	 * @return id of user
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets new user id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return users name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets user name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return nick name of user
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets users nick
	 * 
	 * @param nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * @return avatar url for current user
	 */
	public Avatar getAvatar() {
		return avatar;
	}

	/**
	 * Sets avatar url
	 * 
	 * @param avatar
	 */
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

}