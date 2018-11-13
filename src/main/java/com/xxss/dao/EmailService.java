package com.xxss.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xxss.entity.Card;
import com.xxss.entity.Email;

public interface EmailService extends JpaRepository<Email, String>{
	
	Page<Email> findBytargetEmail(String targetEmail,Pageable pageable);
	
	Email findByid(String id);
	
	List<Email> findBytargetEmail(String targetEmail);
}
