package com.syncano.android.lib.modules.administrators;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;

/**
 * Adds a new administrator to current instance. Account with admin_email must exist in Syncano. Only Admin permission
 * role can add new administrators.
 */
public class ParamsAdminNew extends Params {
	/** Email of admin to add */
	@Expose
	@SerializedName(value = "admin_email")
	private String adminEmail;
	/** Initial role for current instance (see role.get()) */
	@Expose
	@SerializedName(value = "role_id")
	private String roleId;

    /** Message that will be sent along with invitation to instance. */
    @Expose
    private String message;

	/**
	 * 
	 * @param adminEmail
	 *            Email of admin to add
	 * @param roleId
	 *            Initial role for current instance (see role.get())
     * @param message
     *            Message that will be sent along with invitation to instance.
	 */
	public ParamsAdminNew(String adminEmail, String roleId, String message) {
		setAdminEmail(adminEmail);
		setRoleId(roleId);
        setMessage(message);
	}

	@Override
	public String getMethodName() {
		return "admin.new";
	}

	/**
	 * @return email of desired administrator
	 */
	public String getAdminEmail() {
		return adminEmail;
	}

	/**
	 * Sets new email for administrator
	 * 
	 * @param adminEmail
	 *            email of administrator
	 */
	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	/**
	 * @return role id of desired administrator
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * Sets new role id for administrator
	 * 
	 * @param roleId
	 *            role id of administrator
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

    /**
     * Get message that will be sent along with invitation to instance.
     * @return Message related with invitation.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message that will be sent along with invitation to instance.
     * @param message
     *          Message related with invitation.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}