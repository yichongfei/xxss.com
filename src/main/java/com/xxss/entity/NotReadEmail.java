package com.xxss.entity;

import com.xxss.util.AccountCache;

public class NotReadEmail {
	
	private Email email;
	
	private Account account;

	public NotReadEmail(Email email) {
		this.email = email;
		this.account =AccountCache.accountMap.get(email.getFromEmail());
	}
	
	
	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}


	public Account getAccount() {
		return account;
	}


	public void setAccount(Account account) {
		this.account = account;
	}

	
	
}
