package com.xxss.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxss.entity.Account;
import com.xxss.entity.Pay;

public interface PayService extends JpaRepository<Pay, Long> {
	Pay findByemail(String email);
	
	Pay findByid(String id);

}
