package com.xxss.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "article")
public class Article {
	@Id
	private String id;
	
	//作者ID
	private String accountId;
	
	//作者的名字
	private String accountName;
	
	//文章标题
	private String title;
	
	//文章内容
	private String context;
	
	//文章发布时间
	private long publishTime;
	
	//点赞次数
	private int admireTimes;
	
	//读取人数
	private int readTimes;
	
	//文章的分类
	private String category;
	
	//文章是否公开 0是没有公开,1是公开
	private String isPublic; 
	
	//价格,售卖的价格
	private int price;
	
	//是否付费,0免费,1付费
	private String isCost;
	

	//是否置顶  0是没有置顶,1是置顶
	private String isStick;
	//帖子是否是精品,0不精品,1是精品
	private String isGood;
	//城市
	private String city;
	//付费内容
	private String costContext;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(long publishTime) {
		this.publishTime = publishTime;
	}

	public int getAdmireTimes() {
		return admireTimes;
	}

	public void setAdmireTimes(int admireTimes) {
		this.admireTimes = admireTimes;
	}

	public int getReadTimes() {
		return readTimes;
	}

	public void setReadTimes(int readTimes) {
		this.readTimes = readTimes;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getIsStick() {
		return isStick;
	}

	public void setIsStick(String isStick) {
		this.isStick = isStick;
	}

	public String getIsGood() {
		return isGood;
	}

	public void setIsGood(String isGood) {
		this.isGood = isGood;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getIsCost() {
		return isCost;
	}

	public void setIsCost(String isCost) {
		this.isCost = isCost;
	}

	public String getCostContext() {
		return costContext;
	}

	public void setCostContext(String costContext) {
		this.costContext = costContext;
	}
	
	
	
	
	
	
	
}
