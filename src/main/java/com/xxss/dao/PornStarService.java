package com.xxss.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xxss.entity.PornStar;
import com.xxss.entity.Video;

public interface PornStarService extends JpaRepository<PornStar, Long> {
	
	List<PornStar> findBycountry(Pageable page ,String country);
	
	PornStar findById(String id);
}
