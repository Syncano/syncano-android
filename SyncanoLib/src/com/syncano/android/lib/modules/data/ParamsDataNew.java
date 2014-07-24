package com.syncano.android.lib.modules.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.syncano.android.lib.modules.Params;
import com.syncano.android.lib.modules.Response;

/**
 * Creates a new Data Object. collection_id/collection_key parameter means that one can use either one of them -
 * collection_id or collection_key.
 */
public class ParamsDataNew extends Params {
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
	/** Data key */
	@Expose
	@SerializedName(value = "dataKey")
	private String dataKey;
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
    /** data1 */
    @Expose
    private Integer data1;
    /** data2 */
    @Expose
    private Integer data2;
    /** data3 */
    @Expose
    private Integer data3;
	/** Image base64 */
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

	public ParamsDataNew(String projectId, String collectionId, String collectionKey, String state) {
		setProjectId(projectId);
		setCollectionId(collectionId);
		setCollectionKey(collectionKey);
		setState(state);
	}

	@Override
	public Response instantiateResponse() {
		return new ResponseDataNew();
	}

	@Override
	public String getMethodName() {
		return "data.new";
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
	 * 
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
	 * Sets link
	 * 
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

    /**
     * @return data1
     */
    public Integer getData1() {
        return data1;
    }

    /**
     * Sets data1
     * @param data1
     */
    public void setData1(Integer data1) {
        this.data1 = data1;
    }

    /**
     * @return data2
     */
    public Integer getData2() {
        return data2;
    }

    /**
     * Sets data2
     * @param data2
     */
    public void setData2(Integer data2) {
        this.data2 = data2;
    }

    /**
     * @return data3
     */
    public Integer getData3() {
        return data3;
    }

    /**
     * Sets data3
     * @param data3
     */
    public void setData3(Integer data3) {
        this.data3 = data3;
    }

	/**
	 * 
	 * @return image in base 64
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets image in base 64
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 
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
	public void setData_key(String dataKey) {
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

}