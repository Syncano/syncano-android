package com.syncano.android.lib.modules.folders;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Folder;

/**
 * Response after creating folder.
 */
public class ResponseFolderNew extends Response {
	/** new folder */
	private Folder folder;

	/**
	 * @return new folder
	 */
	public Folder getFolder() {
		return folder;
	}

	/**
	 * Sets new folder
	 * 
	 * @param folder
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
}