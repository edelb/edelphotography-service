package com.edel.entities;

import java.util.List;

public class PhotoEntity {
	
	private String id;
	private String secret;
	private String server;
	private int farm;
	private String title;
	private List<SizeEntity> sizes;
	
	public PhotoEntity() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getFarm() {
		return farm;
	}

	public void setFarm(int farm) {
		this.farm = farm;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public List<SizeEntity> getSizes() {
		return sizes;
	}

	public void setSizes(List<SizeEntity> sizes) {
		this.sizes = sizes;
	}

	@Override
	public String toString() {
		return "PhotoEntity [id=" + id + ", secret=" + secret + ", server=" + server + ", farm=" + farm + ", title="
				+ title + ", sizes=" + sizes + "]";
	}
	
}
