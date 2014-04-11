package com.syncano.android.lib.objects;

import java.io.Serializable;

/**
 * Represents Image object from Syncano Api
 */
public class Image implements Serializable {
	private static final long serialVersionUID = 2468714676125234433L;
	/** image url */
	private String image;
	/** image width */
	private Integer image_width;
	/** image height */
	private Integer image_height;
	/** thumbnail url */
	private String thumbnail;
	/** thumbnail width */
	private Integer thumbnail_width;
	/** thumbnail height */
	private Integer thumbnail_height;
	/** source url */
	private String source_url;

	/**
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
	public void setImage_width(Integer imageWidth) {
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
	public void setImage_height(Integer imageHeight) {
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
	 * @return thumbnail width
	 */
	public Integer getThumbnailWidth() {
		return thumbnail_width;
	}

	/**
	 * Sets thumbnail width
	 * 
	 * @param thumbnailWidth
	 */
	public void setThumbnailWidth(Integer thumbnailWidth) {
		this.thumbnail_width = thumbnailWidth;
	}

	/**
	 * @return thumbnail height
	 */
	public Integer getThumbnailHeight() {
		return thumbnail_height;
	}

	/**
	 * Sets thumbnail height
	 * 
	 * @param thumbnailHeight
	 */
	public void setThumbnailHeight(Integer thumbnailHeight) {
		this.thumbnail_height = thumbnailHeight;
	}

	/**
	 * @return source url
	 */
	public String getSourceUrl() {
		return source_url;
	}

	/**
	 * Sets source url(optional)
	 * 
	 * @param sourceUrl
	 */
	public void setSource_url(String sourceUrl) {
		this.source_url = sourceUrl;
	}
}