package com.syncano.android.lib.objects;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

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

	/** data id */
	private String id;
	/** created at date */
	private Date created_at;
	/** updated at date */
	private Date updated_at;
	/** folder */
	private String folder;
	/** state */
	private String state;
	/** user associated with date */
	private User user;
	/** key */
	private String key;
	/** title */
	private String title;
	/** text */
	private String text;
	/** data link url */
	private String link;
	/** source url */
	private String source_url;
	/** Image data associated with message. */
	private Image image;
	/** additional map */
	private HashMap<String, String> additional;
	/** children collection */
	private Data[] children;
	/** number of children */
	private Integer children_count;

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
	public Date getCreated_at() {
		return created_at;
	}

	/**
	 * Sets created at date
	 * 
	 * @param createdAt
	 */
	public void setCreatedAt(Date createdAt) {
		this.created_at = createdAt;
	}

	/**
	 * @return updated at date
	 */
	public Date getUpdatedAt() {
		return updated_at;
	}

	/**
	 * Sets updated at date
	 * 
	 * @param updatedAt
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updated_at = updatedAt;
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
	public Integer getChildren_count() {
		return children_count;
	}

	/**
	 * Sets children count
	 * 
	 * @param childrenCount
	 */
	public void setChildrenCount(Integer childrenCount) {
		this.children_count = childrenCount;
	}

	/**
	 * Deep cloning this object using fact that it is Serializable. May return null if there where problems.
	 */
	public Data clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Data) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
}