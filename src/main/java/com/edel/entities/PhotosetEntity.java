package com.edel.entities;

import java.util.List;

public class PhotosetEntity {

	private String id;
	private String title;
	private String description;
	private String dateCreated;
	private String dateUpdated;
	private List<PhotoEntity> photos;

	public PhotosetEntity() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public List<PhotoEntity> getPhotos() {
		return photos;
	}

	public void setPhotos(List<PhotoEntity> photos) {
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "PhotosetEntity [id=" + id + ", title=" + title + ", description=" + description + ", dateCreated="
				+ dateCreated + ", dateUpdated=" + dateUpdated + ", photo=" + photos + "]";
	}

}
