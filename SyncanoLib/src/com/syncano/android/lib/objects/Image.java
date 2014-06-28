package com.syncano.android.lib.objects;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents Image object from Syncano Api
 */
public class Image implements Serializable {
	private static final long serialVersionUID = 2468714676125234433L;
	/** image url */
	@Expose
	private String image;
	/** image width */
	@Expose
	@SerializedName(value = "image_width")
	private Integer imageWidth;
	/** image height */
	@Expose
	@SerializedName(value = "image_height")
	private Integer imageHeight;
	/** thumbnail url */
	@Expose
	private String thumbnail;
	/** thumbnail width */
	@Expose
	@SerializedName(value = "thumbnail_width")
	private Integer thumbnailWidth;
	/** thumbnail height */
	@Expose
	@SerializedName(value = "thumbnail_height")
	private Integer thumbnailHeight;
	/** source url */
	@Expose
	@SerializedName(value = "source_url")
	private String sourceUrl;

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
		return imageWidth;
	}

	/**
	 * Sets image width
	 * 
	 * @param imageWidth
	 */
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}

	/**
	 * @return image height
	 */
	public Integer getImageHeight() {
		return imageHeight;
	}

	/**
	 * Sets image height
	 * 
	 * @param imageHeight
	 */
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
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
		return thumbnailWidth;
	}

	/**
	 * Sets thumbnail width
	 * 
	 * @param thumbnailWidth
	 */
	public void setThumbnailWidth(Integer thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}

	/**
	 * @return thumbnail height
	 */
	public Integer getThumbnailHeight() {
		return thumbnailHeight;
	}

	/**
	 * Sets thumbnail height
	 * 
	 * @param thumbnailHeight
	 */
	public void setThumbnailHeight(Integer thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}

	/**
	 * @return source url
	 */
	public String getSourceUrl() {
		return sourceUrl;
	}

	/**
	 * Sets source url(optional)
	 * 
	 * @param sourceUrl
	 */
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
}