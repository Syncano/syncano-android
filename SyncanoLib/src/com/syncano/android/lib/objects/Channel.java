package com.syncano.android.lib.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Channel {
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	@Expose
	private String folder;

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}
}
