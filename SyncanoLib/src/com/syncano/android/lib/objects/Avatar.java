package com.syncano.android.lib.objects;

import java.io.Serializable;

/**
 * Represents Avatar object from Syncano Api
 */
public class Avatar implements Serializable {
	private static final long serialVersionUID = -9163152553338874438L;
	/** image url */
	private String image;
	/** image width */
	private Integer image_width;
	/** image height */
	private Integer image_height;
	/** thumbnail url */
	private String thumbnail;
	/** thumbnail width */
	private String thumbnail_width;
	/** thumbnail height */
	private String thumbnail_height;

	/**
	 * 
	 * @return image url
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Sets image url
	 * 
	 * @param image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * @return image width
	 */
	public Integer getImageWidth() {
		return image_width;
	}

	/**
	 * Sets image width
	 * 
	 * @param imageWidth
	 */
	public void setImageWidth(Integer imageWidth) {
		this.image_width = imageWidth;
	}

	/**
	 * @return image height
	 */
	public Integer getImageHeight() {
		return image_height;
	}

	/**
	 * Sets image height
	 * 
	 * @param imageHeight
	 */
	public void setImageHeight(Integer imageHeight) {
		this.image_height = imageHeight;
	}

	/**
	 * @return thumbnail url
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * Sets thumbnail url
	 * 
	 * @param thumbnail
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * @return Thumbnail width
	 */
	public String getThumbnail_width() {
		return thumbnail_width;
	}

	/**
	 * Sets thumbnail width
	 * 
	 * @param thumbnailWidth
	 */
	public void setThumbnailWidth(String thumbnailWidth) {
		this.thumbnail_width = thumbnailWidth;
	}

	/**
	 * @return Thumbnail height
	 */
	public String getThumbnailHeight() {
		return thumbnail_height;
	}

	/**
	 * Sets thumbnail height
	 * 
	 * @param thumbnailHeight
	 */
	public void setThumbnail_height(String thumbnailHeight) {
		this.thumbnail_height = thumbnailHeight;
	}

}