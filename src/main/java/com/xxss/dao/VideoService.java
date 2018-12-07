package com.xxss.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.xxss.entity.Video;

public interface VideoService extends JpaRepository<Video, Long> {
	Video findById(String id);
	
	Page<Video> findBytitleLike(String title,Pageable page);
	
	List<Video> findBycategory(Pageable page ,String category );
	
	@Query("select count(1) as c from Video where category = ?1")
	int getCountByCategory(String category);
	
	@Query("select count(1) as c from Video where title like  ?1")
	int getCountByTitleLike(String title);
	
	@Query("select count(1) as c from Video")
	int getCountRows();
	
}
