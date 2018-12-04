package com.xxss.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pay")
public class Pay {
	@Id
	private String id;
	
	private String email;
	
	private String vipType;
	
	private long sendTime;
	
	private String status;//订单的状态,0是不成功,1是成功
	
	private String qyType; //支付方式 
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVipType() {
		return vipType;
	}

	public void setVipType(String vipType) {
		this.vipType = vipType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQyType() {
		return qyType;
	}

	public void setQyType(String qyType) {
		this.qyType = qyType;
	}
	
	
	
	
}
