package com.xxss.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "video")
public class Video {

	@Id
	public String id;

	public String title;   //VIDEO名字

	public String mp4Key;  //MP4 在BUCKET中的位置

	public String picKey;  //图片在BUCKET中的位置

	public long uploadTime; //VIDEO上传的时间

	public int playTimes;  //VIDEO被播放的次数

	public int admireTimes; //VIDEO被点赞的次数

	public String category; //VIDEO的分类

	public String owner;   //VIDEO的作者
	
	public String preUrl;   //预播放的URL
	
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

	public String getMp4Key() {
		return mp4Key;
	}

	public void setMp4Key(String mp4Key) {
		this.mp4Key = mp4Key;
	}

	public String getPicKey() {
		return picKey;
	}

	public void setPicKey(String picKey) {
		this.picKey = picKey;
	}

	public long getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(long uploadTime) {
		this.uploadTime = uploadTime;
	}

	public int getPlayTimes() {
		return playTimes;
	}

	public void setPlayTimes(int playTimes) {
		this.playTimes = playTimes;
	}

	public int getAdmireTimes() {
		return admireTimes;
	}

	public void setAdmireTimes(int admireTimes) {
		this.admireTimes = admireTimes;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
	
	
	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public void updateIncreasePlayTimes() {
		this.playTimes ++ ;
	}
	
	
	
	@Override
	public String toString() {
		return "Video [id=" + id + ", title=" + title + ", mp4Key=" + mp4Key + ", picKey=" + picKey + ", uploadTime="
				+ uploadTime + ", playTimes=" + playTimes + ", admireTimes=" + admireTimes + ", category=" + category
				+ ", owner=" + owner + ", preUrl=" + preUrl + "]";
	}

	
	

	
	
	
	
}
