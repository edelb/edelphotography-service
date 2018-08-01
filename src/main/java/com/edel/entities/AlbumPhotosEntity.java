package com.edel.entities;

import java.util.List;

public class AlbumPhotosEntity {
	
	private String photoId;
	private List<DateEntity> dates;
	private List<SizeEntity> sizes;
	
	public AlbumPhotosEntity() {
		super();
	}

	public String getPhotoId() {
		return photoId;
	}

	public void setPhotoId(String photoId) {
		this.photoId = photoId;
	}

	public List<DateEntity> getDates() {
		return dates;
	}

	public void setDates(List<DateEntity> dates) {
		this.dates = dates;
	}

	public List<SizeEntity> getSizes() {
		return sizes;
	}

	public void setSizes(List<SizeEntity> sizes) {
		this.sizes = sizes;
	}

	@Override
	public String toString() {
		return "AlbumPhotosEntity [photoId=" + photoId + ", dates=" + dates + ", sizes=" + sizes + "]";
	}
	
}
