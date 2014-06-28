package com.syncano.android.lib.modules.folders;

import com.google.gson.annotations.Expose;
import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Folder;

/**
 * Return one folder after request.
 * 
 */
public class ResponseFolderGetOne extends Response {
	/** desired folder */
	@Expose
	private Folder folder;

	/**
	 * @return folder
	 */
	public Folder getFolder() {
		return folder;
	}

	/**
	 * Sets folder
	 * 
	 * @param folder
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
}