package com.edel.entities;

public class DateEntity {
	
	private String posted;
	private String taken;
	private String takengranularity;
	private String lastupdate;
	
	public DateEntity() {
		super();
	}

	public String getPosted() {
		return posted;
	}

	public void setPosted(String posted) {
		this.posted = posted;
	}

	public String getTaken() {
		return taken;
	}

	public void setTaken(String taken) {
		this.taken = taken;
	}

	public String getTakengranularity() {
		return takengranularity;
	}

	public void setTakengranularity(String takengranularity) {
		this.takengranularity = takengranularity;
	}

	public String getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(String lastupdate) {
		this.lastupdate = lastupdate;
	}

	@Override
	public String toString() {
		return "DateEntity [posted=" + posted + ", taken=" + taken + ", takengranularity=" + takengranularity
				+ ", lastupdate=" + lastupdate + "]";
	}

}
