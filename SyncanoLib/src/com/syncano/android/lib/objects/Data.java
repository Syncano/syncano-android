package com.syncano.android.lib.objects;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Data object from Syncano Api
 */
public class Data implements Serializable, Cloneable {

	private static final long serialVersionUID = 2547073334888179053L;
	/** Data state */
	public static final String PENDING = "Pending";
	/** Data state */
	public static final String MODERATED = "Moderated";
	/** Data state */
	public static final String REJECTED = "Rejected";

	/** parent id */
	@Expose
	@SerializedName(value = "parent_id")
	private String parentId;
	/** data id */
	@Expose
	private String id;
	/** created at date */
	@Expose
	@SerializedName(value = "created_at")
	private Date createdAt;
	/** updated at date */
	@Expose
	@SerializedName(value = "updated_at")
	private Date updatedAt;
	/** folder */
	@Expose
	private String folder;
	/** state */
	@Expose
	private String state;
	/** user associated with date */
	@Expose
	private User user;
	/** key */
	@Expose
	private String key;
	/** title */
	@Expose
	private String title;
	/** text */
	@Expose
	private String text;
	/** data link url */
	@Expose
	private String link;
	@Expose
	private Integer data1;
	@Expose
	private Integer data2;
	@Expose
	private Integer data3;
	/** source url */
	@Expose
	@SerializedName(value = "source_url")
	private String sourceUrl;
	/** Image data associated with message. */
	@Expose
	private Image image;
	/** additional map */
	@Expose
	private HashMap<String, String> additional;
	/** children collection */
	@Expose
	private Data[] children;
	/** number of children */
	@Expose
	@SerializedName(value = "children_count")
	private Integer childrenCount;

	/**
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return created at date
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets created at date
	 * 
	 * @param createdAt
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return updated at date
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Sets updated at date
	 * 
	 * @param updatedAt
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
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
	 * @return user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets user
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Sets key
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
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

	public Integer getData1() {
		return data1;
	}

	public void setData1(Integer data1) {
		this.data1 = data1;
	}

	public Integer getData2() {
		return data2;
	}

	public void setData2(Integer data2) {
		this.data2 = data2;
	}

	public Integer getData3() {
		return data3;
	}

	public void setData3(Integer data3) {
		this.data3 = data3;
	}

	/**
	 * @return data link url
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets data link url
	 * 
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
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
	 * @return Image data associated with message.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Sets image data associated with message.
	 * 
	 * @param image
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return additional map
	 */
	public HashMap<String, String> getAdditional() {
		return additional;
	}

	/**
	 * Sets additional map
	 * 
	 * @param additional
	 */
	public void setAdditional(HashMap<String, String> additional) {
		this.additional = additional;
	}

	/**
	 * @return children array
	 */
	public Data[] getChildren() {
		return children;
	}

	/**
	 * Sets children(Data array)
	 * 
	 * @param children
	 */
	public void setChildren(Data[] children) {
		this.children = children;
	}

	/**
	 * @return children count
	 */
	public Integer getChildrenCount() {
		return childrenCount;
	}

	/**
	 * Sets children count
	 * 
	 * @param childrenCount
	 */
	public void setChildrenCount(Integer childrenCount) {
		this.childrenCount = childrenCount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Deep cloning this object using fact that it is Serializable. May return null if there where problems.
	 */
	public Data clone(Gson gson) {
		return gson.fromJson(gson.toJson(this), this.getClass());
	}
}