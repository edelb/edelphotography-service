package com.edel.entities;

public class SizeEntity {
	
	private String label;
	private int width;
	private int height;
	private String source;
	private String url;
	private String media;
	
	public SizeEntity() {
		super();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

	@Override
	public String toString() {
		return "SizeEntity [label=" + label + ", width=" + width + ", height=" + height + ", source=" + source
				+ ", url=" + url + ", media=" + media + "]";
	}

}
