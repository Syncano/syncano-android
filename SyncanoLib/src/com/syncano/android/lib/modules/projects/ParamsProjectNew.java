package com.syncano.android.lib.modules.projects;

import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Create a new project.
 */

public class ParamsProjectNew extends Params {
	/** New project's name */
	private String name;
	/** New project's description */
	private String description;

	/**
	 * @param name
	 *            New project's name. Cannot be <code>null</code>.
	 */
	public ParamsProjectNew(String name) {
		this.name = name;
	}

	@Override
	public String getMethodName() {
		return "project.new";
	}

	public Response instantiateResponse() {
		return new ResponseProjectNew();
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
	 * @return description
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
}