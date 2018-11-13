package com.xxss.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 充值卡实体类
 * @author Administrator
 *
 */
@Entity
@Table(name = "card")
public class Card {
	@Id
	private String keyWords ;
	
	private String secret;
	
	private double months;
	
	private boolean isAvailable;

	private String email; //点卡的使用者
	
	
	public long getMonthsMillisecond() {
		return  (long)this.months*30*24*60*60*1024;
	}
	
	
	public String getKey() {
		return keyWords;
	}

	public void setKey(String key) {
		this.keyWords = key;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public double getMonths() {
		return months;
	}

	public void setMonths(double months) {
		this.months = months;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
	
	
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	@Override
	public String toString() {
		return "Card [key=" + keyWords + ", secret=" + secret + ", months=" + months + ", isAvailable=" + isAvailable
				+ ", email=" + email + "]";
	}


	public static void main(String[] args) {
		Card card = new Card();
		card.setMonths(3);
		System.out.println(card.getMonthsMillisecond());
		
	}
	
	
}
