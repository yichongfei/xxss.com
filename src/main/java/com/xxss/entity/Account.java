package com.xxss.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * XXSS网站账号实体类
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "account")
public class Account {
	@Id
	private String id;
	
	private String email;// 邮箱

	private String password;// 密码

	private boolean vip;// 是不是VIP

	private long vipDeadline;// vip截止日期

	private String tuiguangURL;
	
	private int tuiguangTimes;
	
	private String name; //账号昵称
	
	private String picPath; //头像路径
	
	private int token ;  //用户的积分
	
	private int level ; //用户的等级
	
	private String description; //描述
	
	private int privilege;//权限, 0是普通权限,1是管理者权限
	
	private long registerTime;//注册时间
	
	private String superaccount;//超级会员
	
	public Account() {
		
	}
	
	public Account(String email,String password) {
		this.id = UUID.randomUUID().toString();
		this.email = email;
		this.password = password;
		this.vip = false;
		this.tuiguangURL =creatTuiguangUrl(id)+"欢迎观看形形色色华人社区,最精彩,等你来";
		this.registerTime = System.currentTimeMillis();
		this.token=0;
	}

	/**
	 * 判断是否过期
	 * 
	 * @return
	 */
	public boolean vipIsAvailable() {
		if (this.vipDeadline - System.currentTimeMillis() > 0) {
			return true;
		}
		return false;
	}

	
	/**
	 * 创建推广链接
	 * @param id
	 * @return
	 */
	public  String creatTuiguangUrl(String id) {
		String tuiguangUrl = "www.xxss2018.com/account/tuiguang?id="+id;
		return tuiguangUrl;
	}
	
	
	/**
	 * 访问有效,推广次数加1;
	 */
	public void updateTuiguangTimes() {
		this.setTuiguangTimes(this.tuiguangTimes+1);
	}
	
	
	
	/**
	 * 用户升级VIP,通过card
	 */
	public void updateVip(Card card) {
		if (this.vipDeadline < System.currentTimeMillis()) {
			this.vipDeadline = System.currentTimeMillis() + card.getMonthsMillisecond();
		}else {
			this.vipDeadline = this.vipDeadline + card.getMonthsMillisecond();
		}
		this.vip = true;
	}
	
	
	/**
	 * 用户升级VIP通过支付宝充值
	 */
	public void updateVip(Pay pay) {
		if (this.vipDeadline < System.currentTimeMillis()) {
			this.vipDeadline = System.currentTimeMillis() + Long.valueOf(pay.getVipType())*30*24*60*60*1000;
		}else {
			this.vipDeadline = this.vipDeadline + Long.valueOf(pay.getVipType())*30*24*60*60*1000;
		}
		this.vip = true;
	}
	
	
	public void checkTuiguangTimes() {
		if(this.tuiguangTimes ==10) {
			Card card = new Card();
			card.setMonths(1);
			this.updateVip(card);
		}
		if(this.tuiguangTimes ==30) {
			Card card = new Card();
			card.setMonths(2);
			this.updateVip(card);
		}
		if(this.tuiguangTimes ==60) {
			Card card = new Card();
			card.setMonths(3);
			this.updateVip(card);
		}
		if(this.tuiguangTimes ==100) {
			Card card = new Card();
			card.setMonths(6);
			this.updateVip(card);
		}
		
	}
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public long getVipDeadline() {
		return vipDeadline;
	}

	public void setVipDeadline(long vipDeadline) {
		this.vipDeadline = vipDeadline;
	}

	public int getTuiguangTimes() {
		return tuiguangTimes;
	}

	public void setTuiguangTimes(int tuiguangTimes) {
		this.tuiguangTimes = tuiguangTimes;
	}

	public String getTuiguangURL() {
		return tuiguangURL;
	}

	public void setTuiguangURL(String tuiguangURL) {
		this.tuiguangURL = tuiguangURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public int getMoney() {
		return token;
	}

	public void setMoney(int token) {
		this.token = token;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrivilege() {
		return privilege;
	}

	public void setPrivilege(int privilege) {
		this.privilege = privilege;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public String getSuperaccount() {
		return superaccount;
	}

	public void setSuperaccount(String superaccount) {
		this.superaccount = superaccount;
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	
	
	
}
