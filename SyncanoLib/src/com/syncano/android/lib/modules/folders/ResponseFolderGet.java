package com.syncano.android.lib.modules.folders;

import com.syncano.android.lib.modules.Response;
import com.syncano.android.lib.objects.Folder;

/**
 * Returns folders for specified collection.
 */
public class ResponseFolderGet extends Response {
	/** folder array for specified collection */
	private Folder[] folder;

	/**
	 * @return folder array
	 */
	public Folder[] getFolder() {
		return folder;
	}

	/**
	 * Sets folders array
	 * 
	 * @param folder
	 */
	public void setFolder(Folder[] folder) {
		this.folder = folder;
	}
}