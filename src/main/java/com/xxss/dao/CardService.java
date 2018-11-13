package com.xxss.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xxss.entity.Card;

public interface CardService extends JpaRepository<Card, String>{
	Card findBykeyWords(String key);
}
