package com.xxss.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Email标题
 * @author Administrator
 *
 */
@Entity
@Table(name = "email")
public class Email {
	
	@Id
	private String id ;
	
	//邮件的目标发送地址
	private String targetEmail;
	
	//邮件从哪里来的
	private String fromEmail;
	
	//邮件发送时间
	private long sendTime;
	
	//邮件是否已读
	private String isRead;
	
	//邮件标题
	private String title;
	
	//邮件内容
	private String context;
	
	//是否是官方邮件
	private String isGuanfangEmail;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTargetEmail() {
		return targetEmail;
	}

	public void setTargetEmail(String targetEmail) {
		this.targetEmail = targetEmail;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
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

	public String getIsGuanfangEmail() {
		return isGuanfangEmail;
	}

	public void setIsGuanfangEmail(String isGuanfangEmail) {
		this.isGuanfangEmail = isGuanfangEmail;
	}
	

	
}
