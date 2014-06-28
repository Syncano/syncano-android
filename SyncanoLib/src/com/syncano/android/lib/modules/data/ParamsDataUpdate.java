package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Updates existing Data Object if data with specified data_id or data_key already exists. You can specify how to react
 * if specified Data Object already exists with update_method parameter. Possible options: replace - default value. Will
 * delete all Data Object fields and create a new one in its place (no previous data will remain). merge - will not
 * delete/empty previously set data but merge it instead with new data. collection_id/collection_key parameter means
 * that one can use either one of them - collection_id or collection_key.
 */
public class ParamsDataUpdate extends Params {
	/** Project id */
	@Expose
	@SerializedName(value = "project_id")
	private String projectId;
	/** Collection id */
	@Expose
	@SerializedName(value = "collection_id")
	private String collectionId;
	/** Collection key */
	@Expose
	@SerializedName(value = "collection_key")
	private String collectionKey;
	/** Data id */
	@Expose
	@SerializedName(value = "data_id")
	private String dataId;
	/** Data key */
	@Expose
	@SerializedName(value = "data_key")
	private String dataKey;
	/** Update method */
	@Expose
	@SerializedName(value = "update_method")
	private String updateMethod;
	/** User name */
	@Expose
	@SerializedName(value = "user_name")
	private String userName;
	/** Source url */
	@Expose
	@SerializedName(value = "source_url")
	private String sourceUrl;
	/** Title */
	@Expose
	private String title;
	/** Text */
	@Expose
	private String text;
	/** Link */
	@Expose
	private String link;
	/** Image */
	@Expose
	private String image;
	/** Image url */
	@Expose
	@SerializedName(value = "image_url")
	private String imageUrl;
	/** Folder */
	@Expose
	private String folder;
	/** State */
	@Expose
	private String state;
	/** Parent id */
	@Expose
	@SerializedName(value = "parent_id")
	private String parentId;

	/**
	 * Default constructor
	 * 
	 * @param projectId
	 *            project id
	 * @param collectionId
	 *            collection id, can be <code>null</code>
	 * @param collectionKey
	 *            collection key, can be <code>null</code>
	 * @param dataId
	 *            data id
	 * @param dataKey
	 *            data key
	 */
	public ParamsDataUpdate(String projectId, String collectionId, String collectionKey, String dataId, String dataKey) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setDataId(dataId);
		setDataKey(dataKey);
	}

	@Override
	public String getMethodName() {
		return "data.update";
	}

	public Response instantiateResponse() {
		return new ResponseDataUpdate();
	}

	/**
	 * @return project id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * Sets project id
	 * 
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return collection key
	 */
	public String getCollectionKey() {
		return collectionKey;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collectionKey
	 */
	public void setCollectionKey(String collectionKey) {
		this.collectionKey = collectionKey;
	}

	/**
	 * @return collection id
	 */
	public String getCollectionId() {
		return collectionId;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	/**
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets user name
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return source url
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}

	/**
	 * Sets source url
	 * 
	 * @param sourceUrl
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets text
	 * 
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets link url
	 * 
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return image in base64
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets image in base64
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return image url
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Sets image url
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Sets folder
	 * 
	 * @param folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
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
	 * @return data key
	 */
	public String getDataKey() {
		return dataKey;
	}

	/**
	 * Sets data key
	 * 
	 * @param dataKey
	 */
	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	/**
	 * @return parent id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets parent id
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return data id
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * Sets data id
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * @return update method
	 */
	public String getUpdateMethod() {
		return updateMethod;
	}

	/**
	 * Sets update method
	 * 
	 * @param updateMethod
	 */
	public void setUpdateMethod(String updateMethod) {
		this.updateMethod = updateMethod;
	}
}