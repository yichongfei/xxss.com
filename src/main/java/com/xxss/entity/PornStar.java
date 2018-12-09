package com.xxss.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pornstar")
public class PornStar {
	@Id
	private String id;
	
	private String pornStarName;
	
	private String picUrl;
	
	private String previewUrl;
	
	private int rank;
	
	private String url;
	
	private String country;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPornStarName() {
		return pornStarName;
	}

	public void setPornStarName(String pornStarName) {
		this.pornStarName = pornStarName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public static void main(String[] args) {
			String str = "x-pornstar/2018-12-09/Ai Sayama/preview.mp4";
			System.out.println(str.replace("preview.mp4", "1.jpg"));
	}
	
}
