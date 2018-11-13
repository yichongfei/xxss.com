package com.xxss.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.xxss.entity.Article;
import com.xxss.entity.ArticleReply;

public interface ArticleReplyService extends JpaRepository<ArticleReply, Long>{
	Page<ArticleReply> findByarticleid(String articleid,Pageable pageable);
}
