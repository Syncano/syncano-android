package com.syncano.android.lib.objects;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Administrator object from Syncano Api
 */
public class Admin implements Serializable {
	private static final long serialVersionUID = -6806030969092528126L;
	/** administrator id */
	@Expose
	private String id;
	/** administrator email */
	@Expose
	private String email;
	/** administrator first name */
	@Expose
	@SerializedName(value = "first_name")
	private String firstName;
	/** administrator last name */
	@Expose
	@SerializedName(value = "last_name")
	private String lastName;
	/** administrator last login date */
	@Expose
	@SerializedName(value = "last_login")
	private Date lastLogin;
	/** administrator specific role */
	@Expose
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
		return firstName;
	}

	/**
	 * Sets administrators first name
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Last name of administrator
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets administrators last name
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return last login
	 */
	public Date getLastLogin() {
		return lastLogin;
	}

	/**
	 * Sets last login
	 * 
	 * @param lastLogin
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
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