package com.syncano.android.lib.modules.data;

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
	private String project_id;
	/** Collection id */
	private String collection_id;
	/** Collection key */
	private String collection_key;
	/** Data id */
	private String data_id;
	/** Data key */
	private String data_key;
	/** Update method */
	private String update_method;
	/** User name */
	private String user_name;
	/** Source url */
	private String source_url;
	/** Title */
	private String title;
	/** Text */
	private String text;
	/** Link */
	private String link;
	/** Image */
	private String image;
	/** Image url */
	private String image_url;
	/** Folder */
	private String folder;
	/** State */
	private String state;
	/** Parent id */
	private String parent_id;

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
		return project_id;
	}

	/**
	 * Sets project id
	 * 
	 * @param projectId
	 */
	public void setProjectId(String projectId) {
		this.project_id = projectId;
	}

	/**
	 * @return collection key
	 */
	public String getCollectionKey() {
		return collection_key;
	}

	/**
	 * Sets collection key
	 * 
	 * @param collectionKey
	 */
	public void setCollectionKey(String collectionKey) {
		this.collection_key = collectionKey;
	}

	/**
	 * @return collection id
	 */
	public String getCollectionId() {
		return collection_id;
	}

	/**
	 * Sets collection id
	 * 
	 * @param collectionId
	 */
	public void setCollectionId(String collectionId) {
		this.collection_id = collectionId;
	}

	/**
	 * @return user name
	 */
	public String getUserName() {
		return user_name;
	}

	/**
	 * Sets user name
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.user_name = userName;
	}

	/**
	 * @return source url
	 */
	public String getSourceUrl() {
		return source_url;
	}

	/**
	 * Sets source url
	 * 
	 * @param sourceUrl
	 */
	public void setSourceUrl(String sourceUrl) {
		this.source_url = sourceUrl;
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
		return image_url;
	}

	/**
	 * Sets image url
	 * 
	 * @param imageUrl
	 */
	public void setImageUrl(String imageUrl) {
		this.image_url = imageUrl;
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
		return data_key;
	}

	/**
	 * Sets data key
	 * 
	 * @param dataKey
	 */
	public void setDataKey(String dataKey) {
		this.data_key = dataKey;
	}

	/**
	 * @return parent id
	 */
	public String getParentId() {
		return parent_id;
	}

	/**
	 * Sets parent id
	 * 
	 * @param parentId
	 */
	public void setParentId(String parentId) {
		this.parent_id = parentId;
	}

	/**
	 * @return data id
	 */
	public String getDataId() {
		return data_id;
	}

	/**
	 * Sets data id
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.data_id = dataId;
	}

	/**
	 * @return update method
	 */
	public String getUpdateMethod() {
		return update_method;
	}

	/**
	 * Sets update method
	 * 
	 * @param updateMethod
	 */
	public void setUpdateMethod(String updateMethod) {
		this.update_method = updateMethod;
	}
}