package com.xxss.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxss.entity.Account;

public interface AccountService extends JpaRepository<Account, Long> {
	Account findByemail(String email);
	
	Account findByid(String id);

}
