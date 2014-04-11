package com.syncano.android.lib.objects;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents Administrator object from Syncano Api
 */
public class Admin implements Serializable {
	private static final long serialVersionUID = -6806030969092528126L;
	/** administrator id */
	private String id;
	/** administrator email */
	private String email;
	/** administrator first name */
	private String first_name;
	/** administrator last name */
	private String last_name;
	/** administrator last login date */
	private Date last_login;
	/** administrator specific role */
	private Role role;

	/**
	 * @return id of current administrator
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id of current administrator
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return email of administrator
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets administrator email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return First name of administrator
	 */
	public String getFirstName() {
		return first_name;
	}

	/**
	 * Sets administrators first name
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.first_name = firstName;
	}

	/**
	 * @return Last name of administrator
	 */
	public String getLastName() {
		return last_name;
	}

	/**
	 * Sets administrators last name
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.last_name = lastName;
	}

	/**
	 * @return last login
	 */
	public Date getLastLogin() {
		return last_login;
	}

	/**
	 * Sets last login
	 * 
	 * @param lastLogin
	 */
	public void setLastLogin(Date lastLogin) {
		this.last_login = lastLogin;
	}

	/**
	 * @return Administrator role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Sets administrator role
	 * 
	 * @param role
	 */
	public void setRole(Role role) {
		this.role = role;
	}

}