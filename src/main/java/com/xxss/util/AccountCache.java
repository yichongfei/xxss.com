package com.xxss.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.xxss.dao.AccountService;
import com.xxss.entity.Account;

public class AccountCache {
	
	public static final ConcurrentHashMap<String,Account> accountMap = new ConcurrentHashMap<>();
	
	private AccountService accountservice;
	
	public AccountCache(AccountService accountservice) {
		this.accountservice=accountservice;
	}
	
	public void updateAccountMap() {
		//首先清空缓存
		accountMap.clear();
		List<Account> list = accountservice.findAll();
		for (Account account : list) {
			accountMap.put(account.getId(), account);
			accountMap.put(account.getEmail(), account);
		}
	}
}
